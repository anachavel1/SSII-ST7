# clientsocket.py

import socket
import secrets, hmac, hashlib, rsa, time

HOST = "127.0.0.1"  # The server's hostname or IP address
PORT = 3030  # The port used by the server

nonce_storage = set()

def peticion_segura_clave_mac(s):
    # Generamos clave privada y publica para pedir con ella la key
    publicKey, privateKey = rsa.newkeys(512)
    s.sendall(bytes(str(publicKey.n), 'utf-8'))
    time.sleep(1)
    s.sendall(bytes(str(publicKey.e), 'utf-8'))
    data1 = s.recv(1024)
    return rsa.decrypt(data1, privateKey).decode()

def filtra_mensaje(nonce, mac, mac_recibido):
    alerta = False
    if nonce in nonce_storage:
        print("Mensaje no válido. Posible ataque REPLAY.")
        alerta = True
    elif str(mac) != mac_recibido :
        print("Mensje no válido. Posible ataque MAN IN THE MIDDLE.")
        alerta = True
    else:
        nonce_storage.add(nonce)
    return (alerta)

def transaccion(origen, destino, cantidad):
    
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        # Conecta y envía el mensaje
        s.connect((HOST, PORT))
        key_mac = peticion_segura_clave_mac(s)
        nonce = secrets.token_urlsafe()
        mensaje = nonce + origen + destino + cantidad.zfill(10)
        mac = hmac.new(bytes(key_mac, 'utf-8'), bytes(mensaje, 'utf-8'), hashlib.sha256).digest()
        s.sendall(bytes(mensaje + str(mac), 'utf-8'))
        
        # Recibe y filtra la respuesta
        respuesta = s.recv(1024).decode("utf-8")        
        if filtra_mensaje(respuesta[0:43],
                          str(hmac.new(bytes(key_mac, 'utf-8'), bytes(respuesta[0:52], 'utf-8'), hashlib.sha256).digest()),
                          respuesta[52:]):
            print("Ataque detectado")

    print(f"Recibido:\n"+ respuesta[43:52])


buen_cliente1 = ("ES00000000000000000000", "ES11111111111111111111", "200")
buen_cliente2 = ("ES00000000000000000000", "ES11111111111111111111", "200")
buen_cliente3 = ("ES11111111111111111111", "ES22222222222222222222", "5555")

transaccion(buen_cliente1[0], buen_cliente1[1], buen_cliente1[2])
transaccion(buen_cliente2[0], buen_cliente2[1], buen_cliente2[2])
transaccion(buen_cliente3[0], buen_cliente3[1], buen_cliente3[2])
