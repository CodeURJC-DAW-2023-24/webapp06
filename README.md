# 📚 Inforum

## 🚀 Our Team

| Nombre                 | Email                             | Github                |
|------------------------|-----------------------------------|-----------------------|
| Jorge Carbonero Asín   | <j.carbonero.2021@alumnos.urjc.es>| JorgeCarbonero        |
| Juan Salguero Ibarrola | <j.salguero.2023@alumnos.urjc.es> | jsalgueroibarrola     |
| Adrián Dueñas Minguez  | <a.duenas.2021@alumnos.urjc.es>   | AdriDM-urjc           |
| Miguel Quero           | <m.quero.2021@alumnos.urjc.es>    | miguel-quero          |
| David Moreno Martín    | <d.moreno1.2019@alumnos.urjc.es>  | DavidMorenoo          |

## 🛠️ Tools

- [Trello](https://trello.com/invite/espaciodetrabajodeuser7a9834432e7b029c3b125e94682e86cc/ATTI8fd1de6f9901d870951c48995f21a12bA10FBC62)
- Confluence

## 📂 Entities

![Class diagram](./images/phase_0/entities.jpg)

- **User:** The User entity contains personal and authentication information about the user.
- **Thread:** Represents a conversation within a Forum.
- **Post:** Represents a message within a Thread.
- **Forum:** Represents a specific category where users can create threads.

## 🔐 User Permissions

- **Anonymous**: The basic user, does not have an account. Can enter forums, read threads and register an account.
- **Registered**: Has created an account and is logged in. Can read and take part in forums by creating threads and posts, and edit and delete his account.
- **Administrator**: Has all the functionality of a registered user plus the ability to create, edit or delete forums, threads, and messages.

## 🖼️ Images

- Users can have profile pictures and posts can have an image attached to them.

## 📊 Graphs

- Users with an account can visualize how many threads they have created each month of the past year, the same can be done with posts.

## 📧 Complementary Technology

- We will send new users emails to activate their accounts.
- Users that want to change their passwords will be sent a password recovery email.

## 📈 Algorithm

- Anonymous users will be shown a list of the most trending threads in the last 2 days.
- For registered users, this list will be filtered to only show threads from forums where they are active.



<br><br>

# Phase 1
## Screens

### Home Screen

Home screen of our application, from here, we will be able to enter to any of the available categories, and in addition, we will also be able to observe the categories in trendig. This home screen also has 2 buttons at the top right, these buttons are to log in or register if the user wishes.

Note that it is not necessary to be registered to navigate between the categories, but your actions will be limited.


![Home Screen](./images/phase_1/home.png)

### Login Screen

Login screen, here we can log in if we already have an account created, to do so, we must enter our username and password, once logged in, the user will be moved back to the home screen.

In case we do not have an account, we will also have the option to go to the registration screen from this same screen.

![Login Screen](./images/phase_1/login.png)

### Register Screen

Introducir breve descipción

![Register Screen](./images/phase_1/signup.png)

### forum Screen
Introducir breve descipción

![Forum Screen](./images/phase_1/forum.png)