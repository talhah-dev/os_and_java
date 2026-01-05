#!/usr/bin/env bash
# ==========================================================
# ==========================================================
# ==========================================================
# ==========================================================
# ==========================================================
# GROUP-11: System Health Check & Report Generator
# (Windows Git Bash / MINGW64 friendly)
#
# Requirements covered (OS Lab):
# - CPU, Memory, Disk stats
# - Running processes + "system load" (CPU load + process count)
# - Human-readable report
# - Email report to recipient
# Extra for high marks:
# - Config file, thresholds, HEALTHY/WARNING/CRITICAL
# - TXT + HTML reports
# - Logging
# - Menu + CLI
# ==========================================================
# ==========================================================
# ==========================================================
# ==========================================================

set -u

# -------------------- DEFAULT SETTING --------------
CONFIG_FILE="./health.conf"
REPORT_DIR="./reports"
LOG_FILE="./health_check.log"

TOP_N=5

# Thresholds (percent)
CPU_WARN=80
CPU_CRIT=95
MEM_WARN=80
MEM_CRIT=95
DISK_WARN=80
DISK_CRIT=90

# Email (SMTP via PowerShell)
EMAIL_ENABLE="no"           # yes/no
EMAIL_TO=""
SMTP_SERVER=""
SMTP_PORT="587"
SMTP_USER=""
SMTP_PASS=""
EMAIL_SUBJECT_PREFIX="[Health Report]"

# Last generated report path (we store it so option "Email last report" is easy)
LAST_REPORT_FILE="./last_report_path.txt"

# -------------------- HELPERS --------------------
now() { date "+%Y-%m-%d %H:%M:%S"; }

have_cmd() { command -v "$1" >/dev/null 2>&1; }

mkdir -p "$REPORT_DIR"

# Load config if exists
load_config() {
  if [[ -f "$CONFIG_FILE" ]]; then
    # shellcheck disable=SC1090
    source "$CONFIG_FILE"
  fi
  mkdir -p "$REPORT_DIR"
}

# Simple logging
write_log() {
  local msg="$1"
  echo "$(now) - $msg" >> "$LOG_FILE"
}

# -------------------- WINDOWS FRIENDLY METRICS --------------------
get_hostname() {
  hostname 2>/dev/null || echo "N/A"
}

get_os_name() {
  if have_cmd systeminfo; then
    systeminfo 2>/dev/null | grep -i "OS Name" | head -n 1 | cut -d: -f2- | sed 's/^ *//'
  else
    echo "N/A"
  fi
}

get_boot_time() {
  if have_cmd systeminfo; then
    systeminfo 2>/dev/null | grep -i "System Boot Time" | head -n 1 | cut -d: -f2- | sed 's/^ *//'
  else
    echo "N/A"
  fi
}

# CPU load %
get_cpu_load_percent() {
  if have_cmd wmic; then
    local v
    v="$(wmic cpu get loadpercentage /value 2>/dev/null | grep -i LoadPercentage | head -n 1 | cut -d= -f2 | tr -d '\r')"
    [[ -n "${v:-}" ]] && echo "$v" || echo "N/A"
  else
    echo "N/A"
  fi
}

# Memory percent
get_memory_percent() {
  if have_cmd wmic; then
    local total_kb free_kb used_kb pct
    total_kb="$(wmic OS get TotalVisibleMemorySize /value 2>/dev/null | grep -i TotalVisibleMemorySize | cut -d= -f2 | tr -d '\r')"
    free_kb="$(wmic OS get FreePhysicalMemory /value 2>/dev/null | grep -i FreePhysicalMemory | cut -d= -f2 | tr -d '\r')"

    if [[ -n "${total_kb:-}" && -n "${free_kb:-}" && "$total_kb" -gt 0 ]]; then
      used_kb=$((total_kb - free_kb))
      pct=$((used_kb * 100 / total_kb))
      echo "$pct"
    else
      echo "N/A"
    fi
  else
    echo "N/A"
  fi
}

get_memory_details() {
  if have_cmd wmic; then
    local total_kb free_kb used_kb pct total_mb used_mb
    total_kb="$(wmic OS get TotalVisibleMemorySize /value 2>/dev/null | grep -i TotalVisibleMemorySize | cut -d= -f2 | tr -d '\r')"
    free_kb="$(wmic OS get FreePhysicalMemory /value 2>/dev/null | grep -i FreePhysicalMemory | cut -d= -f2 | tr -d '\r')"

    if [[ -n "${total_kb:-}" && -n "${free_kb:-}" && "$total_kb" -gt 0 ]]; then
      used_kb=$((total_kb - free_kb))
      pct=$((used_kb * 100 / total_kb))
      total_mb=$((total_kb / 1024))
      used_mb=$((used_kb / 1024))
      echo "${used_mb}MB / ${total_mb}MB (${pct}%)"
    else
      echo "N/A"
    fi
  else
    echo "N/A"
  fi
}

# Disk report (df works in Git Bash for Windows drives)
disk_report_table() {
  if have_cmd df; then
    echo "Drive/Mount        Used%   Size   Used   Avail"
    echo "------------------------------------------------"
    df -h 2>/dev/null | awk 'NR==1{next} {printf "%-16s %-6s %-6s %-6s %-6s\n", $NF, $5, $2, $3, $4}'
  else
    echo "N/A"
  fi
}

get_max_disk_percent() {
  if have_cmd df; then
    df -h 2>/dev/null | awk 'NR==1{next} {gsub("%","",$5); if($5+0>max) max=$5} END{if(max=="") print "N/A"; else print max}'
  else
    echo "N/A"
  fi
}

# Process count + top processes
get_process_count() {
  if have_cmd ps; then
    # -W exists on some Git Bash versions; if not, normal ps works
    if ps -W >/dev/null 2>&1; then
      ps -W -e 2>/dev/null | tail -n +2 | wc -l | tr -d ' '
    else
      ps -e 2>/dev/null | tail -n +2 | wc -l | tr -d ' '
    fi
  else
    echo "N/A"
  fi
}

top_processes_cpu() {
  if have_cmd ps; then
    if ps -W >/dev/null 2>&1; then
      ps -W -eo pid,%cpu,%mem,comm 2>/dev/null | sort -k2 -nr | head -n $((TOP_N + 1))
    else
      ps -eo pid,%cpu,%mem,comm 2>/dev/null | sort -k2 -nr | head -n $((TOP_N + 1))
    fi
  else
    echo "N/A"
  fi
}

top_processes_mem() {
  if have_cmd ps; then
    if ps -W >/dev/null 2>&1; then
      ps -W -eo pid,%mem,%cpu,comm 2>/dev/null | sort -k2 -nr | head -n $((TOP_N + 1))
    else
      ps -eo pid,%mem,%cpu,comm 2>/dev/null | sort -k2 -nr | head -n $((TOP_N + 1))
    fi
  else
    echo "N/A"
  fi
}

# -------------------- HEALTH STATUS (HEALTHY/WARNING/CRITICAL) --------------------
get_status_and_issues() {
  local cpu="$1"
  local mem="$2"
  local disk="$3"

  local status="HEALTHY"
  local issues=""

  # CPU
  if [[ "$cpu" != "N/A" ]]; then
    if (( cpu >= CPU_CRIT )); then
      status="CRITICAL"
      issues+="CPU is very high (${cpu}%)\n"
    elif (( cpu >= CPU_WARN )); then
      [[ "$status" == "HEALTHY" ]] && status="WARNING"
      issues+="CPU is high (${cpu}%)\n"
    fi
  fi

  # Memory
  if [[ "$mem" != "N/A" ]]; then
    if (( mem >= MEM_CRIT )); then
      status="CRITICAL"
      issues+="Memory is very high (${mem}%)\n"
    elif (( mem >= MEM_WARN )); then
      [[ "$status" == "HEALTHY" ]] && status="WARNING"
      issues+="Memory is high (${mem}%)\n"
    fi
  fi

  # Disk
  if [[ "$disk" != "N/A" ]]; then
    if (( disk >= DISK_CRIT )); then
      status="CRITICAL"
      issues+="Disk is very high (max ${disk}%)\n"
    elif (( disk >= DISK_WARN )); then
      [[ "$status" == "HEALTHY" ]] && status="WARNING"
      issues+="Disk is high (max ${disk}%)\n"
    fi
  fi

  # print two lines: status then issues
  echo "$status"
  echo -e "$issues"
}

# -------------------- REPORT GENERATION (TXT + HTML) --------------------
generate_txt_report() {
  local id report_file
  id="$(date +%Y%m%d_%H%M%S)"
  report_file="${REPORT_DIR}/health_report_${id}.txt"

  local hname osname boot cpu mem_pct mem_detail maxdisk pcount
  hname="$(get_hostname)"
  osname="$(get_os_name)"
  boot="$(get_boot_time)"
  cpu="$(get_cpu_load_percent)"
  mem_pct="$(get_memory_percent)"
  mem_detail="$(get_memory_details)"
  maxdisk="$(get_max_disk_percent)"
  pcount="$(get_process_count)"

  local status issues
  status="$(get_status_and_issues "$cpu" "$mem_pct" "$maxdisk" | head -n 1)"
  issues="$(get_status_and_issues "$cpu" "$mem_pct" "$maxdisk" | tail -n +2)"

  {
    echo "========================================"
    echo "SYSTEM HEALTH CHECK REPORT (Group-11)"
    echo "========================================"
    echo "Generated     : $(now)"
    echo "Computer      : $hname"
    echo "OS            : $osname"
    echo "Boot Time     : $boot"
    echo
    echo "OVERALL STATUS: $status"
    if [[ -n "$issues" ]]; then
      echo "Issues:"
      echo -e "$issues"
    else
      echo "Issues: None"
    fi
    echo
    echo "----- SYSTEM STATS -----"
    echo "CPU Load %    : $cpu"
    echo "Memory        : $mem_detail"
    echo "Max Disk %    : $maxdisk"
    echo "Process Count : $pcount"
    echo
    echo "----- DISK DETAILS -----"
    disk_report_table
    echo
    echo "----- TOP $TOP_N PROCESSES (CPU) -----"
    echo "PID   CPU%   MEM%   COMMAND"
    echo "-------------------------------------"
    top_processes_cpu
    echo
    echo "----- TOP $TOP_N PROCESSES (MEMORY) -----"
    echo "PID   MEM%   CPU%   COMMAND"
    echo "----------------------------------------"
    top_processes_mem
    echo
    echo "NOTE about 'system load':"
    echo "On Windows Git Bash, Linux load-average is not available."
    echo "So we use CPU Load% + Process Count as system load indicators."
  } | tee "$report_file"

  echo "$report_file" > "$LAST_REPORT_FILE"

  write_log "Report generated: $report_file | Status: $status | CPU:$cpu | MEM:$mem_pct | DISK:$maxdisk | PROCS:$pcount"

  echo
  echo "TXT report saved: $report_file"
}

generate_html_report_from_last_txt() {
  if [[ ! -f "$LAST_REPORT_FILE" ]]; then
    echo "No last report found. Generate report first."
    return
  fi

  local txt_file html_file
  txt_file="$(cat "$LAST_REPORT_FILE" 2>/dev/null || true)"
  if [[ ! -f "$txt_file" ]]; then
    echo "Last report file not found. Generate report again."
    return
  fi

  html_file="${txt_file%.txt}.html"

  {
    echo "<html><head><meta charset='utf-8'><title>Health Report</title>"
    echo "<style>body{font-family:Arial;margin:20px} pre{background:#f5f5f5;padding:10px;border:1px solid #ccc}</style>"
    echo "</head><body>"
    echo "<h2>System Health Check Report (Group-11)</h2>"
    echo "<p>Generated: $(now)</p>"
    echo "<pre>"
    # escape HTML special chars
    sed 's/&/\&amp;/g; s/</\&lt;/g; s/>/\&gt;/g' "$txt_file"
    echo "</pre>"
    echo "</body></html>"
  } > "$html_file"

  write_log "HTML report generated: $html_file"
  echo "HTML report saved: $html_file"
}

# -------------------- EMAIL (PowerShell SMTP) --------------------
send_email_last_report() {
  load_config

  if [[ "$EMAIL_ENABLE" != "yes" ]]; then
    echo "Email is disabled (EMAIL_ENABLE=no). Enable it in health.conf."
    return
  fi

  if [[ ! -f "$LAST_REPORT_FILE" ]]; then
    echo "No last report found. Generate report first."
    return
  fi

  local report_file
  report_file="$(cat "$LAST_REPORT_FILE" 2>/dev/null || true)"
  if [[ ! -f "$report_file" ]]; then
    echo "Report file not found. Generate report again."
    return
  fi

  if [[ -z "${EMAIL_TO:-}" || -z "${SMTP_SERVER:-}" || -z "${SMTP_USER:-}" || -z "${SMTP_PASS:-}" ]]; then
    echo "Email settings missing in health.conf (EMAIL_TO/SMTP_SERVER/SMTP_USER/SMTP_PASS)."
    return
  fi

  if ! have_cmd powershell.exe && ! have_cmd pwsh; then
    echo "PowerShell not found. Cannot send email."
    return
  fi

  local subject
  subject="${EMAIL_SUBJECT_PREFIX} $(get_hostname) - $(now)"

  # Use powershell.exe if available (Windows), else pwsh
  local PS="powershell.exe"
  have_cmd powershell.exe || PS="pwsh"

  # Send-MailMessage works on many systems; simple for OS lab projects.
  # Attachment: the report file.
  "$PS" -NoProfile -Command \
    "Send-MailMessage -To '$EMAIL_TO' -From '$SMTP_USER' -Subject '$subject' -Body 'Health report attached.' -SmtpServer '$SMTP_SERVER' -Port $SMTP_PORT -UseSsl -Credential (New-Object System.Management.Automation.PSCredential('$SMTP_USER',(ConvertTo-SecureString '$SMTP_PASS' -AsPlainText -Force))) -Attachments '$report_file'" \
    >/dev/null 2>&1

  if [[ $? -eq 0 ]]; then
    write_log "Email sent to $EMAIL_TO (attachment: $report_file)"
    echo "Email sent to: $EMAIL_TO"
  else
    write_log "Email failed for $EMAIL_TO (attachment: $report_file)"
    echo "Email failed. Check SMTP settings in health.conf."
  fi
}

# -------------------- VIEW LOG --------------------
view_log() {
  if [[ -f "$LOG_FILE" ]]; then
    echo "----- LAST 50 LOG LINES -----"
    tail -n 50 "$LOG_FILE"
  else
    echo "No log file yet."
  fi
}

# -------------------- MENU --------------------
menu() {
  while true; do
    echo "========== GROUP-11 HEALTH CHECK =========="
    echo "1) Generate TXT report"
    echo "2) Generate HTML report (from last TXT)"
    echo "3) Email last report (SMTP via PowerShell)"
    echo "4) View log (last 50 lines)"
    echo "5) Exit"
    echo "-------------------------------------------"
    read -p "Enter choice (1-5): " choice
    echo

    case "$choice" in
      1) generate_txt_report ;;
      2) generate_html_report_from_last_txt ;;
      3) send_email_last_report ;;
      4) view_log ;;
      5) echo "Bye!"; break ;;
      *) echo "Invalid choice. Try again." ;;
    esac
    echo
  done
}

# -------------------- CLI MODE --------------------
# Examples:
# ./health_check.sh --report
# ./health_check.sh --email
# ./health_check.sh --html
if [[ "${1:-}" == "--report" ]]; then
  load_config
  generate_txt_report
  exit 0
elif [[ "${1:-}" == "--html" ]]; then
  load_config
  generate_html_report_from_last_txt
  exit 0
elif [[ "${1:-}" == "--email" ]]; then
  load_config
  send_email_last_report
  exit 0
fi

# Default: menu
load_config
menu
