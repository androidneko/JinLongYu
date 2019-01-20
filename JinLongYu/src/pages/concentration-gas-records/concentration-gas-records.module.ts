import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { ConcentrationGasRecordsPage } from './concentration-gas-records';

@NgModule({
  declarations: [
    ConcentrationGasRecordsPage,
  ],
  imports: [
    IonicPageModule.forChild(ConcentrationGasRecordsPage),
  ],
})
export class ConcentrationGasRecordsPageModule {}
