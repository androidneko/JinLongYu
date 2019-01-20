import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { RegularCustodyShowPage } from './regular-custody-show';

@NgModule({
  declarations: [
    RegularCustodyShowPage,
  ],
  imports: [
    IonicPageModule.forChild(RegularCustodyShowPage),
  ],
})
export class RegularCustodyShowPageModule {}
