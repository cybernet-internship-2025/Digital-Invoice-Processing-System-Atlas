FROM postgres:16-alpine

#init script
COPY init-multiple-databases.sh /docker-entrypoint-initdb.d/
RUN chmod +x /docker-entrypoint-initdb.d/init-multiple-databases.sh