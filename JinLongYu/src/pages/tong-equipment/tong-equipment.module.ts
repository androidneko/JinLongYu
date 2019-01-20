import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { TongEquipmentPage } from './tong-equipment';
import { MultiPickerModule } from '../../../node_modules/ion-multi-picker';

@NgModule({
  declarations: [
    TongEquipmentPage,
  ],
  imports: [
    IonicPageModule.forChild(TongEquipmentPage),
    MultiPickerModule
  ],
})
export class TongEquipmentPageModule {}
