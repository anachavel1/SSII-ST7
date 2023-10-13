# clientsocket.py

import socket
import secrets, hmac, hashlib, rsa, time

HOST = "127.0.0.1"  # The server's hostname or IP address
PORT = 3030  # The port used by the server


def transaccion(origen, destino, cantidad):
    # Generamos clave privada y publica para pedir con ella la key
    publicKey, privateKey = rsa.newkeys(512)
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        cantidad = cantidad.zfill(10)
        
        s.connect((HOST, PORT))
        publicKey_n = publicKey.n
        publicKey_e = publicKey.e
        s.sendall(bytes(str(publicKey_n), 'utf-8'))
        #print("Enviada primera parte de la clave pública")
        time.sleep(1)
        s.sendall(bytes(str(publicKey_e), 'utf-8'))
        #print("Enviada segunda parte de la clave pública")
        data1 = s.recv(1024)
        #print("Recibida la informacion de la clave MAC")

        
        key_mac = rsa.decrypt(data1, privateKey).decode()
        #print(f"Key MAC recibida: " + key_mac)

        # Desde Python3.6 existe el módulo secrets
        # Con él podemos generar numeros aleatorios criptograficamente fuertes aptos para gestionar autenticación, contraseñas o tokens de seguridad.
        nonce = secrets.token_urlsafe()

        #input("Indique la cuenta de destino (codigo + 20 dígitos)")
        mensaje = nonce + origen + destino + cantidad
        #print("mensaje:"+ str(len(mensaje)) +"\n" +
        #      "nonce:" + str(len(nonce)) + "\n" +
        #      "mensaje:"+ str(len(origen)) +"\n" +
        #      "mensaje:"+ str(len(destino)) +"\n" +
        #      "cantidad:"+ str(len(cantidad)) +"\n" )
        #print("EL MENSAJE ES: " + mensaje)

        mac = hmac.new(bytes(key_mac, 'utf-8'), bytes(mensaje, 'utf-8'), hashlib.sha256).digest()

    #with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    #    s.connect((HOST, PORT))
        s.sendall(bytes(mensaje + str(mac), 'utf-8'))
        data2 = s.recv(1024)

    print(f"Received:\n"+ data2.decode('utf-8'))


buen_cliente1 = ("ES00000000000000000000", "ES11111111111111111111", "200")
buen_cliente2 = ("ES00000000000000000000", "ES11111111111111111111", "200")
buen_cliente3 = ("ES11111111111111111111", "ES22222222222222222222", "5555")

transaccion(buen_cliente1[0], buen_cliente1[1], buen_cliente1[2])
transaccion(buen_cliente2[0], buen_cliente2[1], buen_cliente2[2])
transaccion(buen_cliente3[0], buen_cliente3[1], buen_cliente3[2])
