import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { StoreInfoPage } from './store-info';
import { ComponentsModule } from '../../components/components.module';

@NgModule({
  declarations: [
    StoreInfoPage,
  ],
  imports: [
    IonicPageModule.forChild(StoreInfoPage),
    ComponentsModule
  ],
})
export class StoreInfoPageModule {}
