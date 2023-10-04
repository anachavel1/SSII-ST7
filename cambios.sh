#!/bin/bash

carpeta="./carpeta/"
haiku="Este es un haiku de ejemplo.\nEspero que te guste.\nBash scripting es divertido."

for ((i=0; i<6; i++)); do
  # Generar un número aleatorio entre 0 y 1000
  numero_aleatorio=$(shuf -i 0-1000 -n 1)

  # Construir el nombre del archivo
  nombre_archivo="archivo-$(printf "%04d" $numero_aleatorio)"

  # Ruta completa del archivo
  archivo="$carpeta$nombre_archivo"

  # Sobrescribir el contenido del archivo con el haiku
  echo -e "$haiku" > "$archivo"

  # Esperar 5 minutos antes de la próxima modificación
  sleep 300
done

