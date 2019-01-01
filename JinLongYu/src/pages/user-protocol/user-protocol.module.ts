import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { UserProtocolPage } from './user-protocol';

@NgModule({
  declarations: [
    UserProtocolPage,
  ],
  imports: [
    IonicPageModule.forChild(UserProtocolPage),
  ],
})
export class UserProtocolPageModule {}
