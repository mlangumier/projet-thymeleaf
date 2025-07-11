# Projet Thymeleaf

En partant de cette base de projet, le but sera de réaliser avec Spring Boot et Thymeleaf les fonctionnalités de gestion
d'un panier.

## Lancer le projet

Grâce à la dépendance `spring-boot-docker-compose`, un container MySQL se lance automatiquement au démarrage de
l'application. L'application peut être lancée par les moyens habituels : le bouton de démarrage sur l'IDE, ou la
commande suivante :

```shell
  mvn spring-boot:run
```

Pour empêcher Spring de démarrer un serveur Docker au lancement de l'application, il suffit de commenter le contenu des fichiers `application.properties` et `compose.yaml` ou retirer la dépendance du fichier `pom.xml` (bien penser à re-compiler le projet, après ça).

## Fonctionnalités attendues

* [x] Créer une petite classe de fixture permettant de remplir la base de données avec des produits et des users
  (pourquoi pas via un CommandLineRunner)
* [x] Affichage de la liste des produits paginés avec un bouton/form "Add To Cart"
* [ ] Créer un CartService qui contiendra le panier permettant :
    * [ ] D'ajouter un produit au panier, si le produit n'est pas déjà dedans
    * [x] De retirer un produit du panier
    * [x] De récupérer le prix total du panier
    * [ ] De valider le panier, ce qui impliquera de le vider et de baisser le stock des produits qui sont dedans
    * [x] Le panier doit être lié à la session.
* [x] Créer une page `/cart` dans laquelle on pourra visualiser les produits dans son panier, le total et le gérer. Les
  différentes requêtes HTTP déclencheront des méthodes du service depuis le contrôleur

### Fonctionnalités bonus

* [ ] Mettre en place un bouton de déconnexion
* [ ] Gérer la quantité de produit dans le panier (plusieurs manières : soit avec un objet supplémentaire qui a le
  produit et la quantité, soit avec une Map) et donc permettre d'ajouter plusieurs fois le même produit, voir d'ajouter
  plusieurs produits à la fois
* [ ] Faire que le panier persiste pour le User connecté (ça demandera de faire pas mal de truc)
