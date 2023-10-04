#!/bin/bash

# Verifica que se haya proporcionado el mes como argumento
if [ -z "$1" ]; then
    echo "Error: Debes proporcionar el mes como argumento (número del 1 al 12)."
    exit 1
fi

# Verifica que el mes esté en el rango de 1 a 12
if [ "$1" -lt 1 ] || [ "$1" -gt 12 ]; then
    echo "Error: El mes debe estar en el rango del 1 al 12."
    exit 1
fi

# Obtiene el nombre del mes en formato abreviado (por ejemplo, "Oct" para octubre)
month=$(date -d "$1/1" "+%b")

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

