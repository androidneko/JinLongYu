import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { RmAirShowPage } from './rm-air-show';

@NgModule({
  declarations: [
    RmAirShowPage,
  ],
  imports: [
    IonicPageModule.forChild(RmAirShowPage),
  ],
})
export class RmAirShowPageModule {}
