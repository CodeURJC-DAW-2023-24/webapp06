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
