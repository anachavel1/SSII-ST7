#!/bin/bash

carpeta="./carpeta/"
haiku="Este es un bonito haiku de ejemplo.\nEspero que te guste.\nBash scripting es divertido."

# Declarar un array para mantener un registro de los números aleatorios
declare -a numeros_generados

for ((i=0; i<6; i++)); do
  # Generar tres números aleatorios únicos entre 0 y 1000
  while true; do
    numero_aleatorio1=$(shuf -i 0-2000 -n 1)
    if [[ ! " ${numeros_generados[@]} " =~ " $numero_aleatorio1 " ]]; then
      numeros_generados+=($numero_aleatorio1)
      break
    fi
  done

  while true; do
    numero_aleatorio2=$(shuf -i 0-2000 -n 1)
    if [[ ! " ${numeros_generados[@]} " =~ " $numero_aleatorio2 " ]]; then
      numeros_generados+=($numero_aleatorio2)
      break
    fi
  done

  while true; do
    numero_aleatorio3=$(shuf -i 0-2000 -n 1)
    if [[ ! " ${numeros_generados[@]} " =~ " $numero_aleatorio3 " ]]; then
      numeros_generados+=($numero_aleatorio3)
      break
    fi
  done

  # Construir los nombres de los archivos
  nombre_archivo1="archivo-$(printf "%04d" $numero_aleatorio1)"
  nombre_archivo2="archivo-$(printf "%04d" $numero_aleatorio2)"
  nombre_archivo3="archivo-$(printf "%04d" $numero_aleatorio3)"

  # Rutas completas de los archivos
  archivo1="$carpeta$nombre_archivo1"
  archivo2="$carpeta$nombre_archivo2"
  archivo3="$carpeta$nombre_archivo3"
  
  # Sobrescribir el contenido del primer archivo con el haiku
  echo "Ataque nº $i"
  date
  echo -e "$haiku" > "$archivo1"
  echo "Archivo modificado: $archivo1"

  # Eliminar el segundo archivo aleatorio
  rm -f "$archivo2"
  echo "Archivo eliminado: $archivo2"

  # Agregar permisos de ejecución al tercer archivo aleatorio
  chmod +x "$archivo3"
  echo "Permisos de ejecución agregados a: $archivo3"

  # Esperar 5 minutos antes de la próxima modificación
  sleep 10m
done

echo "Done"

./summarize.sh

sleep 2

cat resumen_alertas_Oct.txt
