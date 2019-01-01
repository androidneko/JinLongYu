import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { TyacRecordsPage } from './tyac-records';
import { PipesModule } from '../../pipes/pipes.module';

@NgModule({
  declarations: [
    TyacRecordsPage,
  ],
  imports: [
    IonicPageModule.forChild(TyacRecordsPage),
    PipesModule
  ],
})
export class TyacRecordsPageModule {}
