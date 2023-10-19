# serversocket.py

import socket
import secrets, hmac, hashlib, rsa

HOST = "127.0.0.1"  # Standard loopback interface address (localhost)
PORT = 3030  # Port to listen on (non-privileged ports are > 1023)

nonce_storage = set()

def filtra_mensaje(nonce, mac, mac_recibido):
    alerta = False
    if nonce in nonce_storage:
        print("Transacción no válida. Posible ataque REPLAY.")
        mensaje = "Rechazada."
        alerta = True
    
    elif str(mac) != mac_recibido :
        print("Transacción no válida. Posible ataque MAN IN THE MIDDLE.")
        mensaje = "Rechazada."
        alerta = True

    else:
        nonce_storage.add(nonce)
        mensaje = "Aceptada "
    return (mensaje, alerta)

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.bind((HOST, PORT))
    while True:
        s.listen()
        conn, addr = s.accept()
        with conn:
            print(f"Connected by {addr}")
            #Este nivel representa cada cliente
            
            # Esta sección es la verificación de identidad de cada cliente
            while True:
                data1 = conn.recv(1024)
                #print("Recibida primera parte de la clave pública")
                if not data1:
                    continue
                key_mac = secrets.token_urlsafe()
                publicKey_n = int(data1.decode('utf-8'))
                break
            while True:
                data2 = conn.recv(1024)
                #print("Recibida segunda parte de la clave pública")
                if not data2:
                    continue
                key_mac = secrets.token_urlsafe()
                publicKey_e = int(data2.decode('utf-8'))
                publicKey = rsa.PublicKey(publicKey_n, publicKey_e)
                encrypted_key = rsa.encrypt(key_mac.encode('utf-8'), publicKey)
                conn.sendall(encrypted_key)
                break

            # Esta sección es la transacción de cada cliente    
            while True:
                data3 = conn.recv(1024)
                data_str = data3.decode("utf-8")
                if not data3:
                    continue
                            
                # Primero calculamos el MAC del mensaje 
                mensaje = data_str[0:97]
                #print("EL MENSAJE ES: " + mensaje)
                mac = hmac.new(bytes(key_mac, 'utf-8'), bytes(mensaje, 'utf-8'), hashlib.sha256).digest()
                # Tomamos las distintas partes del mensaje
                nonce_recibido = mensaje[0:43]
                origen = mensaje[43:65]
                destino = mensaje[65:87]
                cantidad = mensaje[87:97]
                mac_recibido = data_str[97:]
                mensaje_local, alerta =  filtra_mensaje(nonce_recibido, mac, mac_recibido)
                nonce_local = secrets.token_urlsafe()
                mac_local = hmac.new(bytes(key_mac, 'utf-8'), bytes(nonce_local + mensaje_local, 'utf-8'), hashlib.sha256).digest()
                conn.sendall(bytes(nonce_local + mensaje_local + str(mac_local) , 'utf-8'))
                #print("Envia:\n"+str(mac_local)+"\n"+nonce_local+"\n"+mensaje_local)
                if alerta:
                    break
                print("Transacción válida: \n" + str(data3) + "\n")
                
                break
                    
            


