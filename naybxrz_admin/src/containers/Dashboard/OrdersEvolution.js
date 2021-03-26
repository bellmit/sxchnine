import React, {Component} from 'react';
import { ResponsiveLine } from '@nivo/line';
import './OrderEvolution.css';


class OrdersEvolution extends Component {

    render() {
        const myData = [
            {
                "id": "shipped",
                "color": "hsl(140, 88%, 43%)",
                "data": [
                    {
                        "x": "mon",
                        "y": 10
                    },
                    {
                        "x": "tue",
                        "y": 0
                    },
                    {
                        "x": "wed",
                        "y": 1
                    },
                    {
                        "x": "thu",
                        "y": 0
                    },
                    {
                        "x": "fri",
                        "y": 2
                    },
                    {
                        "x": "sat",
                        "y": 5
                    },
                    {
                        "x": "sun",
                        "y": 1
                    }
                ]
            },
            {
                "id": "refused",
                "color": "hsl(358, 100%, 63%)",
                "data": [
                    {
                        "x": "mon",
                        "y": 0
                    },
                    {
                        "x": "tue",
                        "y": 0
                    },
                    {
                        "x": "wed",
                        "y": 2
                    },
                    {
                        "x": "thu",
                        "y": 1
                    },
                    {
                        "x": "fri",
                        "y": 0
                    },
                    {
                        "x": "sat",
                        "y": 0
                    },
                    {
                        "x": "sun",
                        "y": 4
                    }
                ]
            },
            {
                "id": "confirmed",
                "color": "hsl(209, 88%, 54%)",
                "data": [
                    {
                        "x": "mon",
                        "y": 1
                    },
                    {
                        "x": "tue",
                        "y": 4
                    },
                    {
                        "x": "wed",
                        "y": 10
                    },
                    {
                        "x": "thu",
                        "y": 0
                    },
                    {
                        "x": "fri",
                        "y": 2
                    },
                    {
                        "x": "sat",
                        "y": 1
                    },
                    {
                        "x": "sun",
                        "y": 1
                    }
                ]
            }
        ]

        return (
            <div className="chart-evolution">
                <ResponsiveLine
                    data={myData}
                    margin={{top: 50, right: 110, bottom: 50, left: 60}}
                    colors={{ datum: 'color' }}
                    xScale={{type: 'point'}}
                    yScale={{type: 'linear', min: 'auto', max: 'auto', stacked: false, reverse: false}}
                    yFormat=" >-.2f"
                    axisTop={null}
                    axisRight={null}
                    axisBottom={{
                        orient: 'bottom',
                        tickSize: 5,
                        tickPadding: 5,
                        tickRotation: 0,
                        legend: 'orders evolution',
                        legendOffset: 36,
                        legendPosition: 'middle'
                    }}
                    axisLeft={{
                        orient: 'left',
                        tickSize: 5,
                        tickPadding: 5,
                        tickRotation: 0,
                        legend: 'count',
                        legendOffset: -40,
                        legendPosition: 'middle'
                    }}
                    pointSize={7}
                    pointColor={{from: 'color'}}
                    pointBorderWidth={1}
                    pointBorderColor={{from: 'serieColor'}}
                    pointLabelYOffset={-12}
                    useMesh={true}
                    legends={[
                        {
                            anchor: 'bottom-right',
                            direction: 'column',
                            justify: false,
                            translateX: 100,
                            translateY: 0,
                            itemsSpacing: 0,
                            itemDirection: 'left-to-right',
                            itemWidth: 80,
                            itemHeight: 20,
                            itemOpacity: 0.75,
                            symbolSize: 12,
                            symbolShape: 'circle',
                            symbolBorderColor: 'rgba(0, 0, 0, .5)',
                            effects: [
                                {
                                    on: 'hover',
                                    style: {
                                        itemBackground: 'rgba(0, 0, 0, .03)',
                                        itemOpacity: 1
                                    }
                                }
                            ]
                        }
                    ]}
                />
            </div>
        );
    }
}

export default OrdersEvolution;