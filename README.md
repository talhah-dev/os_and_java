# System Health Check & Report Generator (Group-11)

## Features
- CPU, Memory, Swap, Disk, Load, Uptime
- Top processes by CPU/RAM
- Network info + services check
- Threshold-based status (Healthy/Warning/Critical)
- Reports: TXT + HTML + JSON
- Logging + basic rotation
- Email sending (optional, only on warning/critical)
- Menu mode + CLI mode + Cron mode
- Configurable via `health.conf`

## Run
```bash
chmod +x health_check.sh
./health_check.sh
