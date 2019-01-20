import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { RegularCustodyPage } from './regular-custody';
import { MultiPickerModule } from '../../../node_modules/ion-multi-picker';

@NgModule({
  declarations: [
    RegularCustodyPage,
  ],
  imports: [
    IonicPageModule.forChild(RegularCustodyPage),
    MultiPickerModule
  ],
})
export class RegularCustodyPageModule {}
