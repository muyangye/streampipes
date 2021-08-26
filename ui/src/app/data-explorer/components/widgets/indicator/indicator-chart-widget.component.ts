/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import { Component, OnInit } from '@angular/core';
import { BaseDataExplorerWidget } from '../base/base-data-explorer-widget';
import { LineChartWidgetModel } from '../line-chart/model/line-chart-widget.model';
import { DatalakeRestService } from '../../../../platform-services/apis/datalake-rest.service';
import { WidgetConfigurationService } from '../../../services/widget-configuration.service';
import { ResizeService } from '../../../services/resize.service';
import { DataResult } from '../../../../core-model/datalake/DataResult';
import { DatalakeQueryParameters } from '../../../../core-services/datalake/DatalakeQueryParameters';
import { DatalakeQueryParameterBuilder } from '../../../../core-services/datalake/DatalakeQueryParameterBuilder';
import { Observable, zip } from 'rxjs';
import { IndicatorChartWidgetModel } from './model/indicator-chart-widget.model';

@Component({
  selector: 'sp-data-explorer-indicator-chart-widget',
  templateUrl: './indicator-chart-widget.component.html',
  styleUrls: ['./indicator-chart-widget.component.scss']
})
export class IndicatorChartWidgetComponent extends BaseDataExplorerWidget<IndicatorChartWidgetModel> implements OnInit {

  data = [
    {
      type: 'indicator',
      mode: 'number+delta',
      value: 400,
      number: {prefix: ''},
      delta: {position: 'top', reference: 320},
      domain: {x: [0, 1], y: [0, 1]}
    }
  ];

  graph = {
    layout: {
      font: {
        color: '#FFF',
        family: 'Roboto'
      },
      autosize: true,
      plot_bgcolor: '#fff',
      paper_bgcolor: '#fff',
      margin: {t: 0, b: 0, l: 0, r: 0},
      grid: {rows: 2, columns: 2, pattern: 'independent'},
      template: {
        data: {
          indicator: [
            {
              mode: 'number+delta',
              delta: {reference: 90}
            }
          ]
        }
      }
    },

    config: {
      modeBarButtonsToRemove: ['lasso2d', 'select2d', 'toggleSpikelines', 'toImage'],
      displaylogo: false,
      displayModeBar: false,
      responsive: true
    }
  };

  constructor(dataLakeRestService: DatalakeRestService,
              widgetConfigurationService: WidgetConfigurationService,
              resizeService: ResizeService) {
    super(dataLakeRestService, widgetConfigurationService, resizeService);
  }

  refreshData() {
    if (this.dataExplorerWidget.dataConfig.showDelta) {
      this.data[0].mode = 'number+delta';
      zip(this.getQuery(this.dataExplorerWidget.dataConfig.numberAggregation),
          this.getQuery(this.dataExplorerWidget.dataConfig.deltaAggregation))
          .subscribe(result => {
            this.prepareData(result[0], result[1]);
          });
    } else {
      this.data[0].mode = 'number';
      this.getQuery(this.dataExplorerWidget.dataConfig.numberAggregation).subscribe(result => {
        this.prepareData(result);
      });
    }
  }

  getQuery(aggregation: string): Observable<DataResult> {
    return this.dataLakeRestService.getData(this.dataLakeMeasure.measureName, this.buildQuery(aggregation));
  }

  refreshView() {
    this.updateAppearance();
  }

  prepareData(numberResult: DataResult, deltaResult?: DataResult) {
    this.data[0].value = numberResult.total > 0 ? numberResult.rows[0][1] : '-';
    if (deltaResult) {
      this.data[0].delta.reference = numberResult.total > 0 ? deltaResult.rows[0][1] : '-';
    }
  }

  updateAppearance() {
    this.graph.layout.paper_bgcolor = this.dataExplorerWidget.baseAppearanceConfig.backgroundColor;
    this.graph.layout.plot_bgcolor = this.dataExplorerWidget.baseAppearanceConfig.backgroundColor;
    this.graph.layout.font.color = this.dataExplorerWidget.baseAppearanceConfig.textColor;
  }

  buildQuery(aggregation: string): DatalakeQueryParameters {
    return DatalakeQueryParameterBuilder.create(this.timeSettings.startTime, this.timeSettings.endTime)
        .withColumnFilter([this.dataExplorerWidget.dataConfig.selectedProperty])
        .withAggregationFunction(aggregation)
        .build();
  }

  onResize(width: number, height: number) {
    this.graph.layout.autosize = false;
    (this.graph.layout as any).width = width;
    (this.graph.layout as any).height = height;
  }


}