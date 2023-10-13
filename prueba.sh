#!/bin/bash

# El símbolo "&" hace que el comando se ejecute en segundo plano, a esto se le suele llamar "job" y te permite liberar la terminal y ejecutar otros comandos simultaneamente.
echo -e "\nPrueba iniciada\n"
python3 serversocket.py &
echo -e "\nSevidor iniciado\n"
sleep 1
echo -e "\nPrueba 3 clientes normales\n"
python3 clientsocket.py &
sleep 5
echo -e "\nPrueba ataque man in the middle:\n"
python3 attackmaninmiddle.py &
sleep 5
echo -e "\nPrueba ataque replay:\n"
python3 attackreplay.py &

