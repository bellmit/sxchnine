1- helm repo add jaegertracing https://jaegertracing.github.io/helm-charts
2- helm install jaeger -f jaeger.yaml jaegertracing/jaeger

logs:
NAME: jaeger
LAST DEPLOYED: Sun Mar 21 17:54:34 2021
NAMESPACE: production
STATUS: deployed
REVISION: 1
TEST SUITE: None
NOTES:
###################################################################
### IMPORTANT: Ensure that storage is explicitly configured     ###
### Default storage options are subject to change.              ###
###                                                             ###
### IMPORTANT: The use of <component>.env: {...} is deprecated. ###
### Please use <component>.extraEnv: [] instead.                ###
###################################################################

You can log into the Jaeger Query UI here:

  export NODE_PORT=$(kubectl get --namespace production -o jsonpath="{.spec.ports[0].nodePort}" services jaeger-query)
  export NODE_IP=$(kubectl get nodes --namespace production -o jsonpath="{.items[0].status.addresses[0].address}")
  echo http://$NODE_IP:$NODE_PORT/ 
