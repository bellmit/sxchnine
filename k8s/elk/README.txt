1- helm install elasticsearch -f elasticsearch.yaml elastic/elasticsearch 

logs:
WARNING: "kubernetes-charts.storage.googleapis.com" is deprecated for "stable" and will be deleted Nov. 13, 2020.
WARNING: You should switch to "https://charts.helm.sh/stable" via:
WARNING: helm repo add "stable" "https://charts.helm.sh/stable" --force-update
WARNING: "kubernetes-charts-incubator.storage.googleapis.com" is deprecated for "incubator" and will be deleted Nov. 13, 2020.
WARNING: You should switch to "https://charts.helm.sh/incubator" via:
WARNING: helm repo add "incubator" "https://charts.helm.sh/incubator" --force-update
NAME: elasticsearch
LAST DEPLOYED: Sat Mar 20 11:48:48 2021
NAMESPACE: production
STATUS: deployed
REVISION: 1
NOTES:
1. Watch all cluster members come up.
  $ kubectl get pods --namespace=production -l app=elasticsearch-master -w
2. Test cluster health using Helm test.
  $ helm test elasticsearch
