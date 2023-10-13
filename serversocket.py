# serversocket.py

import socket
import secrets, hmac, hashlib, rsa

HOST = "127.0.0.1"  # Standard loopback interface address (localhost)
PORT = 3030  # Port to listen on (non-privileged ports are > 1023)

nonce_storage = set()
#black_list = set()

#def blacklist(direccion):
#    black_list.add(direccion)
#    print("Pasamos a inhabilitar su dirección. Si piensa que ha sido un error pongase en contacto con los administradores.")



with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.bind((HOST, PORT))
    while True:
        s.listen()
        conn, addr = s.accept()
        with conn:
            print(f"Connected by {addr}")
            #Este nivel representa cada cliente
            
            fin_transaccion = False
            alerta = False
            # Esta sección es la verificación de identidad de cada cliente
            while True:
                data1 = conn.recv(1024)
                print("Recibida primera parte de la clave pública")
                if not data1:
                    continue
                key_mac = secrets.token_urlsafe()
                publicKey_n = int(data1.decode('utf-8'))
                break
            while True:
                data2 = conn.recv(1024)
                print("Recibida segunda parte de la clave pública")
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
                nonce = mensaje[0:43]
                origen = mensaje[43:65]
                destino = mensaje[65:87]
                cantidad = mensaje[87:97]
            
                # Ahora lo comparamos con el MAC recibido
                if  hmac.compare_digest(mac, data3[97:]):
                # str(mac) != data_str[97:] Evitamos usar esta comparación porque la otra es más segura criptograficamente
                    #blacklist(addr)
                    alerta = True
                    print("Transacción no válida. Posible ataque man in the middle.")
            

                #elif addr in black_list:
                    #print("Lo sentimos su direción se encuentra inhabilitada para hacer transferencias pongase en contacto con los administradores.")
            
                # Comprueba si el nonce ya ha sido usado (evita ataque REPLAY)
                elif nonce in nonce_storage:
                    #blacklist(addr)
                    alerta = True
                    print("Transacción no válida. Posible ataque replay.")
                
                # Comprueba que el nonce sea válido
                else:
                    nonce_storage.add(nonce)
                    fin_transaccion = True
                    print("Transacción válida. Muchas gracias.")
                conn.sendall(data3)
                if fin_transaccion or alerta:
                    break
                    
            


