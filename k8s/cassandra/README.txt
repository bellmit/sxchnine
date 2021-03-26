1- kubectl apply -f cassandra-operator.yaml
2- kubectl apply -f https://raw.githubusercontent.com/datastax/cass-operator/v1.6.0/operator/k8s-flavors/gke/storage.yaml
3- kubectl apply -f cassandra.yaml

# observed problem= cpu/memory insufficiant error
