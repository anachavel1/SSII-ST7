# SSII-ST7
Repositorio para las prácticas de SSII.

Descripción de archivos:

borraarchivos
elimina los archivos creados por inicializaarchivos

cambios.sh
Simulación de ataques automatizados, cada 10 minutos modifica un archivo,
elimina otro y cambia los permisos de un tercero. Sin repetir objetivos. 

creacionArchivos.py
Script para crear archivos de test usando python.

inicializaarchivos
Script para crear 2000 archivos de test

intento.py 
Script python para generar un resumen del diario de registros

ossec.conf
Configuración utilizada para usar OSSEC. Ubicada por defecto en /var/ossec/etc/

resumen\_alertas\_Oct.txt
output de summarize, resumen de las alertas del mes de Octubre

summarize
Script para generar un resumen del diario de registros genera resumen\_alertas\_Oct
Recibe un argumento numero para indicar el mes a resumir, o bien por defecto
utiliza el mes actual

