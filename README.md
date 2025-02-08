# 💰 **Cash Back Jackpot** 💰

## 🎯 Objectif du Kata

Ce kata est un challenge sur l'[architecture hexagonale](https://fr.wikipedia.org/wiki/Architecture_hexagonale), dans le
domaine de la grande consommation. 🚀

## ⚠️ **Modalités de participation** ⚠️

Ce kata a deux objectifs :  
1️⃣ Évaluer vos compétences techniques en tant que candidat,  
2️⃣ Servir de base pour votre montée en compétences si vous faites déjà partie de notre équipe 😄.

Il couvre volontairement un **scope large** pour vous donner de la flexibilité. Vous avez plusieurs options en fonction
du temps que vous souhaitez y consacrer :

- ⏱️ **Peu de temps ?** (une soirée) : Faites uniquement le code métier, testé et fonctionnel, avec des adapteurs pour
  vos tests.
- 🕒 **Plus de temps ?** (plusieurs soirées) : Ajoutez une API REST, un Kafka et une persistance fonctionnelle, le tout
  bien testé de bout en bout.
- 🕐 **Beaucoup de temps et l'envie d'aller plus loin ?** : Ajoutez la containerisation de l'application et une pipeline
  CI/CD ! 🚢💻

### 💡 Ce sur quoi vous serez évalués :

- **Tests** : Tout le code livré doit être correctement testé, en couvrant les cas réussis comme échoués.
- **Design et qualité** : On sera très attentifs à la qualité, la lisibilité du code et aussi de vos commits !

---

## 🔧 **Modalités de réalisation**

Pour réaliser ce kata, suivez ces étapes :  
1️⃣ Tirez une branche depuis `main`  
2️⃣ Effectuez vos développements sur cette branche  
3️⃣ Lorsque vous êtes prêt à soumettre, ouvrez une **merge request** vers `main`  
⚠️ **Attention** : Ne fusionnez pas la merge request ! Elle servira de support pour la revue de code.

---

## 🤑️ **Feature 1 : Le Cagnottage**

L'idée est de créer une application de **Cagnottage**.  
À chaque passage en caisse, un pourcentage du montant de l'achat est ajouté à la cagnotte du client.

### Le client devra avoir les éléments suivants :

- Un **numéro de client unique** (format libre)
- Une **adresse mail**
- Un **numéro de téléphone**
- Une **cagnotte** avec :
    - Un **solde**
    - Un **nombre de passages en caisse**
    - Un **état de disponibilité**
- Une fonctionnalité pour **créer** la cagnotte.
- Une fonctionnalité pour **déposer de l'argent** sur la cagnotte.

---

## 👀 **Feature 2 : Visualisation**

Le client doit pouvoir **visualiser** l'état de sa cagnotte à tout moment.  
Il pourra consulter son **solde** et vérifier si sa cagnotte est **disponible**.

### Règle métier :

Une cagnotte est considérée comme **disponible** si :

- Le client a effectué **au moins 3 passages** en caisse, ET
- Le solde de la cagnotte est d’au moins **10 €**.

---

## 📧 **Feature 3 : Le Cash Back**

Lorsque la cagnotte devient **disponible**, une **notification** doit être envoyée au client avec dedans le **chèque-cadeau** dématérialisé.

### Points à retenir :

- Une autre équipe gère l'envoi des chèques-cadeaux. Leur règle d’or : **Rien ne doit être synchrone !**.
- Un **email** doit être envoyé dès que la cagnotte devient disponible.
- Si le solde dépasse un multiple de 10, le chèque sera émis pour ce montant.

---

## 🕐 **Feature 4 : Ajout asynchrone à la cagnotte**

Avec l'explosion du nombre de clients, et pour éviter tout risque de blocage ou de timeout, nous souhaitons que *
*l'ajout d'argent à la cagnotte soit désormais asynchrone**. Cela permettra d'améliorer les performances et la fluidité
du système, tout en garantissant que les commerçants ne rencontrent aucune latence ou problème lors du passage en
caisse.

### Règles métier :

- Lorsqu'un commerçant passe un client en caisse, rien ne doit changer pour lui : l'opération reste **instantanée et
  transparente** de son côté.
- Le calcul et l'ajout du cashback à la cagnotte se feront désormais de façon **asynchrone**, via un **message Kafka**.
- Le commerçant reçoit une confirmation immédiate de la transaction, mais le traitement de l'ajout d'argent à la
  cagnotte est délégué à un **worker asynchrone** qui traitera le message en arrière-plan.
- En cas de succès, le client pourra consulter le montant mis à jour de sa cagnotte lors de sa prochaine consultation.
- En cas de problème ou de délai prolongé, un **système de retry** sera mis en place pour retenter l’opération sans
  impacter l’expérience utilisateur.

---

## 🚀 Bon chance ! 🎉
