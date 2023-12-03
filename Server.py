# serversocket.py

import socket
import secrets, hmac, hashlib, rsa
import base64

HOST = "127.0.0.1"  # Standard loopback interface address (localhost)
PORT = 7070  # Port to listen on (non-privileged ports are > 1023)


with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.bind((HOST, PORT))
    while True:
        print("Server working")
        s.listen()
        conn, addr = s.accept()
        with conn:
            print(f"Connected by {addr}")
            #Este nivel representa cada cliente
            while True:
                data3 = conn.recv(1024)
                data_str = data3.decode("utf-8")
                if not data3:
                    continue

                camas, mesas, sillas, sillones, idEmpleado, firma = data_str.split("|")
                
                
                print(idEmpleado)
                print("Transacción válida: \n" + str(data3) + "\n")
                
                break
                    
            


