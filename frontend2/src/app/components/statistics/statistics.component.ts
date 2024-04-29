import { Component, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { Chart, registerables } from 'chart.js';
import { StatisticsService } from '../../services/statistics-service.service';

@Component({
  selector: 'app-statistics',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.css'],
})
export class StatisticsComponent implements AfterViewInit {
  @ViewChild('chartCanvas') chartCanvas!: ElementRef<HTMLCanvasElement>;
  chart!: Chart;
  currentRange!: string;
  startDate!: Date;
  currentInterval: Interval = Interval.Weekly;
  Interval = Interval;

  constructor(private statsService: StatisticsService) {
    Chart.register(...registerables);
  }

  ngAfterViewInit(): void {
    this.chart = new Chart(this.chartCanvas.nativeElement, {
      type: 'bar',
      data: {
        labels: [
          'Monday',
          'Tuesday',
          'Wednesday',
          'Thursday',
          'Friday',
          'Saturday',
          'Sunday',
        ],
        datasets: [
          {
            label: 'Weekly Posts',
            backgroundColor: 'rgba(75, 192, 192, 0.2)',
            borderColor: 'rgba(75, 192, 192, 1)',
            borderWidth: 1,
            data: [],
          },
          {
            label: 'Weekly Posts',
            backgroundColor: 'rgba(180, 180, 180, 0.2)',
            borderColor: 'rgba(180, 180, 180, 1)',
            borderWidth: 1,
            data: [],
          },
        ],
      },
      options: {
        scales: {
          y: {
            beginAtZero: true,
          },
        },
      },
    });
    this.setInitialWeekRange();
    this.getWeekly();
  }

  async getAnnually() {
    this.currentInterval = Interval.Annually;
    let currentDate = this.formatDate(this.startDate);
    this.statsService
      .getThreads('annually', this.convertDateFormatBackend(currentDate))
      .then((data) => {
        this.chart.data.datasets[1].data = data;
        this.chart.data.datasets[1].label = 'Annually Threads';
        this.chart.update();
      });
    this.statsService
      .getPosts('annually', this.convertDateFormatBackend(currentDate))
      .then((data) => {
        this.chart.data.labels = this.getJsonKeys(data);
        this.chart.data.datasets[0].data = data;
        this.chart.data.datasets[0].label = 'Annually Posts';
        this.chart.update();
      });
  }

  getJsonKeys(json: Object): string[] {
    return Object.keys(json);
  }

  async getWeekly() {
    this.currentInterval = Interval.Weekly;
    let currentDate = this.formatDate(this.startDate);
    this.statsService
      .getThreads('weekly', this.convertDateFormatBackend(currentDate))
      .then((dataThreads) => {
        this.chart.data.datasets[1].data = dataThreads;
        this.chart.data.datasets[1].label = 'Weekly Threads';
        this.chart.update();
      });

    this.statsService
      .getPosts('weekly', this.convertDateFormatBackend(currentDate))
      .then((dataPosts) => {
        this.chart.data.labels = [
          'Monday',
          'Tuesday',
          'Wednesday',
          'Thursday',
          'Friday',
          'Saturday',
          'Sunday',
        ];
        this.chart.data.datasets[0].data = dataPosts;
        this.chart.data.datasets[0].label = 'Weekly Posts';
        this.chart.update();
      });
  }

  async getMonthly() {
    this.currentInterval = Interval.Monthly;
    let currentDate = this.formatDate(this.startDate);
    this.statsService
      .getThreads('monthly', this.convertDateFormatBackend(currentDate))
      .then((dataThreads) => {
        this.chart.data.datasets[1].data = dataThreads;
        this.chart.data.datasets[1].label = 'Monthly Threads';
        this.chart.update();
      });

    this.statsService
      .getPosts('monthly', this.convertDateFormatBackend(currentDate))
      .then((dataPosts) => {
        this.chart.data.labels = [
          'January',
          'February',
          'March',
          'April',
          'May',
          'June',
          'July',
          'August',
          'September',
          'October',
          'November',
          'December',
        ];
        this.chart.data.datasets[0].data = dataPosts;
        this.chart.data.datasets[0].label = 'Monthly Posts';
        this.chart.update();
      });
  }

  changeWeek(direction: number) {
    const today = this.startDate;

    let startDate = new Date(today.setDate(today.getDate() + direction * 7));
    const endDate = new Date(startDate.getTime());
    endDate.setDate(endDate.getDate() + 6);
    this.currentRange = `${this.formatDate(startDate)} - ${this.formatDate(
      endDate
    )}`;
    this.startDate = startDate;
    this.getWeekly();
  }

  setInitialWeekRange() {
    const today = new Date();
    const dayOfWeek = today.getDay();
    const startOffset = (dayOfWeek === 0 ? -6 : 1) - dayOfWeek;
    const startDate = new Date(today.setDate(today.getDate() + startOffset));
    this.startDate = startDate;
    const endDate = new Date(startDate.getTime());
    endDate.setDate(endDate.getDate() + 6);

    this.currentRange = `${this.formatDate(startDate)} - ${this.formatDate(
      endDate
    )}`;
  }

  formatDate(date: Date): string {
    return `${date.getDate().toString().padStart(2, '0')}/${(
      date.getMonth() + 1
    )
      .toString()
      .padStart(2, '0')}/${date.getFullYear()}`;
  }

  convertDateFormatBackend(dateString: string): string {
    let parts = dateString.split('/');
    return `${parts[2]}-${parts[1]}-${parts[0]}`;
  }
}

export enum Interval {
  Monthly = 'monthly',
  Weekly = 'weekly',
  Annually = 'annually',
}
