#!/bin/bash

# Add docker-repo as an alias so we can
# later generate a TLS certificate using
# "docker-repo" as the host name
grep -q docker-repo /etc/hosts || {
    echo 'Adding "127.0.0.1 docker-repo" to /etc/hosts'
    sudo perl -i -pe 'eof && do{chomp; print "$_\n127.0.0.1\tdocker-repo"; exit}' /etc/hosts
}

# Create a directory to hold the TLS cert
[ -d ~/.docker-repo ] || {
    echo "Creating $HOME/.docker-repo for TLS keys"
    mkdir ~/.docker-repo || exit 1
}

# Generate a TLS cert
( cd ~/.docker-repo &&
      openssl req -newkey rsa:4096 -nodes -sha256 -keyout docker-repo.key \
            -x509 -days 365 -out docker-repo.crt \
            -subj "/C=US/ST=LA/L=Santa Monica/O=Tomitribe/OU=IT/CN=docker-repo"
)

# Start the registry
docker run -d \
       --restart=always \
       --name docker-repo \
       -v "$HOME/.docker-repo":/certs \
       -e REGISTRY_HTTP_TLS_CERTIFICATE=/certs/docker-repo.crt \
       -e REGISTRY_HTTP_TLS_KEY=/certs/docker-repo.key \
       -p 5000:5000 \
       registry:2

# Print the process status
docker ps | grep docker-repo