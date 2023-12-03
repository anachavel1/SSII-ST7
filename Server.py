# serversocket.py

import socket
import secrets, hmac, hashlib, rsa
import base64
from Crypto.Signature import pkcs1_15
from Crypto.Hash import SHA256
from Crypto.PublicKey import RSA    

HOST = "127.0.0.1"  # Standard loopback interface address (localhost)
PORT = 7070  # Port to listen on (non-privileged ports are > 1023)

public_key = "-----BEGIN PUBLIC KEY-----\n" + "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxZV1ATT0gdajScIBsqzg\n" + "bUvitYtFatcmWE0ABgbB8DFvPRJj7eDpFn0g43x6ZKa33lSpgqBGCuk0U/tbsOKx\n" + "vile8z8nY8t4Aa3d9Fs5xoU10aVOQenP4D6IMV3svNnbcB3pXoAuDdZtppYZiRHe\n" + "GvISnSi8ZXBnQrJPaIjyjOTO1JvISEs2qIJ+EXY98Ba9ewOigfZCvdLUIaxQ8fkF\n" + "PljOidlcC5jpzsxdh8AyriHu6+5S4u5ceY43iWY6XYhcVY7IZ7BypfqjiawM7IXI\n" + "rTL6C9qQfWdm7r213KH83cR/DkiS220ka58MVfnJbXtbcml7wwPK4N64fOLmwFWC\n" + "tQIDAQAB\n" + "-----END PUBLIC KEY-----"

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
                mensaje, firma = data_str.split("||")
                
                mensaje2 = str(mensaje)

                camas, mesas, sillas, sillones, idEmpleado = mensaje.split("|")
                
                
                print(idEmpleado)
                print("Transacción válida: \n" + str(data3) + "\n")
                
                
                # Suponiendo que ya tienes la clave pública 'public_key' y el mensaje 'message'
                # También suponiendo que la firma 'firma' está en formato Base64

                # Crear una instancia de objeto hash SHA-256
                hash_obj = SHA256.new((mensaje2).encode('utf-8'))

                # Crear una instancia de objeto de clave pública RSA
                rsa_key = RSA.import_key(public_key)
                
                # Decodificar la firma desde Base64
                signature = firma

             
                
                try:
                # Verificar la firma
                    pkcs1_15.new(rsa_key).verify(hash_obj, signature)
                    print("La firma es válida.")
                except (ValueError, TypeError):
                    print("La firma no es válida.")

                
                break
                    
            


