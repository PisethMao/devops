#!/bin/bash
domain_name="$1"
service_port="$2"
if [ -z $domain_name ]; then
    echo "Domain name must not be empty.";
    exit 1
fi
if [ -z $service_port ]; then
    echo "Service port must be provided.";
    exit 1
fi
NGINX_CONF_DIR="/etc/nginx/conf.d"
if grep -rq "server_name.*${domain_name}.piseth.dev" "$NGINX_CONF_DIR"; then
    echo "true";
    echo "File exist! No need to provide another reverse proxy config."
else
    echo "Write the reverse proxy config.";
    echo "1. Write reverse proxy config for service.";
    tee "${NGINX_CONF_DIR}/${domain_name}.conf" > /dev/null << EOF
    server {
        listen 80;
        listen [::]:80;
        server_name ${domain_name}.piseth.dev;
        location / {
            proxy_pass http://localhost:${service_port};
            proxy_set_header Host \$host;
            proxy_set_header X-Real-IP \$remote_addr;
            proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto \$scheme;
            proxy_http_version 1.1;
            proxy_set_header Upgrade \$http_upgrade;
            proxy_set_header Connection upgrade;
        }
    }
EOF
fi
echo "Test and reload the configuration!!!"
sudo nginx -t && sudo nginx -s reload
echo "Add https for the ${domain_name}.piseth.dev"
sudo certbot --nginx -d ${domain_name}.piseth.dev