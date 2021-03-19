1- helm install mysql -f mysql-bitnami.yaml bitnami/mysql

logs:

NAME: mysql
LAST DEPLOYED: Thu Mar 11 13:50:15 2021
NAMESPACE: production
STATUS: deployed
REVISION: 1
TEST SUITE: None
NOTES:
** Please be patient while the chart is being deployed **

Tip:

  Watch the deployment status using the command: kubectl get pods -w --namespace production

Services:

  echo Primary: mysql.production.svc.cluster.local:3306

Administrator credentials:

  echo Username: root
  echo Password : $(kubectl get secret --namespace production mysql -o jsonpath="{.data.mysql-root-password}" | base64 --decode)

To connect to your database:

  1. Run a pod that you can use as a client:

      kubectl run mysql-client --rm --tty -i --restart='Never' --image  docker.io/bitnami/mysql:8.0.23-debian-10-r28 --namespace production --command -- bash

  2. To connect to primary service (read/write):

      mysql -h mysql.production.svc.cluster.local -uroot -p keycloak-db

To upgrade this helm chart:

  1. Obtain the password as described on the 'Administrator credentials' section and set the 'root.password' parameter as shown below:

      ROOT_PASSWORD=$(kubectl get secret --namespace production mysql -o jsonpath="{.data.mysql-root-password}" | base64 --decode)
      helm upgrade mysql bitnami/mysql --set auth.rootPassword=$ROOT_PASSWORD

