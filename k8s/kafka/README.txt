1- kubectl apply -f 'https://strimzi.io/install/latest?namespace=production'
2- kubectl config set-context --current --namespace=production
3- kubectl apply -f kafka-cluster.yaml
4- kubectl get pods -w
5- kubectl apply -f topic-catchup-orders.yaml
6- kubectl apply -f topic-orders.yaml
7 - kubectl apply -f topic-products.yaml
8 - kubectl apply -f topic-users.yaml
9- kubectl apply -f topic-catchup-orders-dlt.yaml
10- kubectl apply -f topic-orders-dlt.yaml
