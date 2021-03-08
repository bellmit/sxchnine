1- kubectl apply -f config/crd/bases/mongodbcommunity.mongodb.com_mongodbcommunity.yaml
2- kubectl apply -k config/rbac/
3- kubectl create -f config/manager/manager.yaml --namespace production
4- kubectl apply -f mongo-community.yaml
