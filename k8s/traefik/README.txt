1- kubectl create namespace traefik
2- kubectl apply -f crd.yaml
3- kubectl apply -f rbac.yaml
4- htpasswd -bc traefik-dashboard-credentials admin P455W0RD00
5- kubectl create secret generic traefik-auth --from-file traefik-dashboard-credentials --namespace=traefik
6- kubectl create secret generic traefik-service-account --from-file=traefik-service-account.json=./sa_key/complete-tube-306819-0e2b9a71989e.json --namespace=traefik
7- kubectl apply -f traefik.yaml

