from cryptography.hazmat.primitives import serialization
from cryptography.hazmat.primitives.asymmetric import rsa

# Generar un par de claves RSA de 2048 bits
private_key = rsa.generate_private_key(
    public_exponent=65537,
    key_size=2048,
)

# Obtener la clave pública
public_key = private_key.public_key()

# Convertir las claves a formato PEM (Privacy Enhanced Mail)
private_pem = private_key.private_bytes(
    encoding=serialization.Encoding.PEM,
    format=serialization.PrivateFormat.TraditionalOpenSSL,
    encryption_algorithm=serialization.NoEncryption()
)

public_pem = public_key.public_bytes(
    encoding=serialization.Encoding.PEM,
    format=serialization.PublicFormat.SubjectPublicKeyInfo
)

# Imprimir las claves en formato PEM
print("Clave Privada (PEM):")
print(private_pem.decode('utf-8'))

print("\nClave Pública (PEM):")
print(public_pem.decode('utf-8'))

