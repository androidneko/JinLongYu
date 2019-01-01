import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { UserCenterPage } from './user-center';
import { ComponentsModule } from '../../components/components.module';

@NgModule({
  declarations: [
    UserCenterPage,
  ],
  imports: [
    IonicPageModule.forChild(UserCenterPage),
    ComponentsModule
  ],
})
export class UserCenterPageModule {}
