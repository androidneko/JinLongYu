import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { PingEquipmentRecordsPage } from './ping-equipment-records';

@NgModule({
  declarations: [
    PingEquipmentRecordsPage,
  ],
  imports: [
    IonicPageModule.forChild(PingEquipmentRecordsPage),
  ],
})
export class PingEquipmentRecordsPageModule {}
