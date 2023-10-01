# la librería 'os' nos permite interactuar con el sistema operativo
import os
import random

# digo que nombre va a tener el directorio en el que se van a guardar los arquivos
directorio = "SSII_Lab1"


# creación del directorio con la función "mkdir"
try:
    os.mkdir(directorio)
except OSError:
    # si el directorio existe salta la siguiente sentencia
    print("La creación del directorio %s falló" % directorio)
else:
    # si el directorio no existe, se muestra por pantalla el siguiente mensaje
    print("Se ha creado el directorio: %s " % directorio)


# creamos los 100 archivos
def genera_archivos():
    # los archivos que se van a crear pueden tener 4 extensiones diferentes
    extension = [".txt", ".jpg", ".java", ".py"]
    for i in range(101):
        arch = random.randrange(4)
        # se asigna aleatoriamente la extensión a los direntes archivos
        nomb_archivo = f"archivo_{i}"+extension[arch]
        # los archivos se guarden en el directorio que he creado anteriormente
        guard_carptea = os.path.join(directorio, nomb_archivo)
        
        with open(guard_carptea, "w") as archivo:
            # el siguiente codigo, añade a los archivos con las extnsiones ".txt", ".py" y ".java"
            # dependiendo de la extensión que sea, se va a ir añadiendo a cada archivo cosas diferentes
            if extension[arch] == ".txt":
                archivo.write(f"Este es el archivo {i}. Se trata de un archivo de texto")
            
            elif extension[arch] == ".py":
                archivo.write(f"#Este es el archivo {i}. Es un script para escribir codigo en Python")
            
            elif extension[arch] == ".java":
                archivo.write(f"//Este es el archivo {i}. Es un script para escribir codigo en Java")


# para que se muestre por consola que se genera correctamente los archivos
def main():
    genera_archivos()
    print("Se han creado los archivos de texto en el directorio:", directorio)

if __name__ == "__main__":
    main()