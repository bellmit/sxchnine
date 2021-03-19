1- kubectl apply -f managed-certificat.yaml
2- gcloud compute ssl-certificates list --global
3- wait until certificate became active
4- check if naybxrz-ingress.yaml contains:
    networking.gke.io/managed-certificates: ##certificate-name
    kubernetes.io/ingress.class: "gce"

5- kubectl apply -f naybxrz-ingress.yaml
