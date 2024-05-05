import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ModalModule } from 'ngx-bootstrap/modal';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AccessDeniedComponent } from './components/access-denied/access-denied.component';
import { CategoriesComponent } from './components/categories/categories.component';
import { EditProfileComponent } from './components/edit-profile/edit-profile.component';
import { ForumComponent } from './components/forum/forum.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { PostModalComponent } from './components/post-modal/post-modal.component';
import { PostReportedComponent } from './components/post-reported/post-reported.component';
import { PostComponent } from './components/post/post.component';
import { ProfileComponent } from './components/profile/profile.component';
import { ReportedPostsComponent } from './components/reported-posts/reported-posts.component';
import { SignUpComponent } from './components/sign-up/sign-up.component';
import { StatisticsComponent } from './components/statistics/statistics.component';
import { ThreadModalComponent } from './components/thread-modal/thread-modal.component';
import { ThreadComponent } from './components/thread/thread.component';
import { UsersComponent } from './components/users/users.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    ForumComponent,
    ThreadComponent,
    LoginComponent,
    SignUpComponent,
    ProfileComponent,
    EditProfileComponent,
    StatisticsComponent,
    UsersComponent,
    ReportedPostsComponent,
    NavbarComponent,
    PostComponent,
    CategoriesComponent,
    PostModalComponent,
    PostReportedComponent,
    AccessDeniedComponent,
    NotFoundComponent,
    ThreadModalComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    ModalModule.forRoot(),
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
