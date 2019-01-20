import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { PingEquipmentPage } from './ping-equipment';
import { MultiPickerModule } from '../../../node_modules/ion-multi-picker';

@NgModule({
  declarations: [
    PingEquipmentPage,
  ],
  imports: [
    IonicPageModule.forChild(PingEquipmentPage),
    MultiPickerModule
  ],
})
export class PingEquipmentPageModule {}
