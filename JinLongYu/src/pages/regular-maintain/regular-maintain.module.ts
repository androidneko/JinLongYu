import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { RegularMaintainPage } from './regular-maintain';

@NgModule({
  declarations: [
    RegularMaintainPage,
  ],
  imports: [
    IonicPageModule.forChild(RegularMaintainPage),
  ],
})
export class RegularMaintainPageModule {}
