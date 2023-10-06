#!/bin/bash

# Verifica si se proporcionó un argumento, si no, usa el mes actual
if [ -z "$1" ]; then
    current_month=$(date +"%m")
    echo "No se proporcionó un mes como argumento. Usando el mes actual: $current_month"
    month="$current_month"
else
    month="$1"
fi

# Obtiene el nombre del mes en formato abreviado (por ejemplo, "Oct" para octubre)
month=$(date -d "$1" "+%b")

# Convierte la primera letra del mes a mayúsculas
month=$(echo "$month" | awk '{print toupper(substr($0,1,1)) tolower(substr($0,2))}')

year=$(date "+%Y")

# Directorio de alertas
alert_dir="/var/ossec/logs/alerts/$year/$month"

# Archivo de salida
output_file="resumen_alertas_$month.txt"

# Inicializa el archivo de salida
> "$output_file"

# Recorre los archivos y extrae la información
for file in "$alert_dir/ossec-alerts-"*; do
    if grep -q "Integrity checksum changed" "$file"; then
        echo "Procesando $file"
        echo "======================================================" >> "$output_file"
        cat "$file" >> "$output_file"
        echo "======================================================" >> "$output_file"
    fi
done

echo "Resumen de alertas de seguridad generado en $output_file"


