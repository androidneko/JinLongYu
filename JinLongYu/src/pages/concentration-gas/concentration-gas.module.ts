import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { ConcentrationGasPage } from './concentration-gas';
import { MultiPickerModule } from '../../../node_modules/ion-multi-picker';

@NgModule({
  declarations: [
    ConcentrationGasPage,
  ],
  imports: [
    IonicPageModule.forChild(ConcentrationGasPage),
    MultiPickerModule
  ],
})
export class ConcentrationGasPageModule {}
