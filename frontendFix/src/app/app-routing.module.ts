import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from './components/home/home.component';
import { ForumComponent } from './components/forum/forum.component';
import { ThreadComponent } from './components/thread/thread.component';
import { LoginComponent } from './components/login/login.component';
import { SignUpComponent } from './components/sign-up/sign-up.component';
import { ProfileComponent } from './components/profile/profile.component';
import { EditProfileComponent } from './components/edit-profile/edit-profile.component';
import { StatisticsComponent } from './components/statistics/statistics.component';
import { UsersComponent } from './components/users/users.component';
import { ReportedPostsComponent } from './components/reported-posts/reported-posts.component';
import { AccessDeniedComponent } from './components/access-denied/access-denied.component';
import { NotFoundComponent } from './components/not-found/not-found.component';

const routes: Routes = [
  { path: '', component: HomeComponent, data: { title: 'Home' } },
  { path: 'f/:forumId', component: ForumComponent, data: { title: 'Forum' } },
  {
    path: 't/:threadId',
    component: ThreadComponent,
    data: { title: 'Thread' },
  },
  { path: 'login', component: LoginComponent, data: { title: 'Login' } },
  { path: 'register', component: SignUpComponent, data: { title: 'Register' } },
  {
    path: 'user/profile/:userName',
    component: ProfileComponent,
    data: { title: 'Profile' },
  },
  {
    path: 'user/edit-profile/:userName',
    component: EditProfileComponent,
    data: { title: 'Edit profile' },
  },
  { path: 'chart', component: StatisticsComponent, data: { title: 'Chart' } },
  { path: 'users', component: UsersComponent, data: { title: 'Users' } },
  {
    path: 'reports',
    component: ReportedPostsComponent,
    data: { title: 'Reports' },
  },
  {
    path: 'accessDenied',
    component: AccessDeniedComponent,
    data: { title: 'Access Denied' },
  },
  {
    path: '404',
    component: NotFoundComponent,
    data: { title: '404' },
  },
  { path: '**', component: NotFoundComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
