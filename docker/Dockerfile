# Utiliza la imagen oficial de MySQL desde Docker Hub como base
FROM mysql:8.0

# Establece las variables de entorno para configurar la base de datos
ENV MYSQL_ROOT_PASSWORD=password
ENV MYSQL_DATABASE=inforumdb
ENV MYSQL_USER=admin
ENV MYSQL_PASSWORD=admin

# Expon el puerto en el que MySQL estará escuchando
EXPOSE 3306

# Comando para iniciar el servidor MySQL
CMD ["mysqld"]


# docker build -t mysql_daw .
# docker run -d --name inforum_mysql -p 3306:3306 mysql_daw --default-authentication-plugin=mysql_native_password
