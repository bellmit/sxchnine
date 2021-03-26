import React, {Component} from 'react';
import {ResponsivePie} from '@nivo/pie';
import {connect} from 'react-redux';
import './OrdersNumberDashboard.css';

class OrdersNumberDashboard extends Component {

    render() {

        const receivedOrders = this.props.ordersNumber;

        const myData = [
            {
                "id": "waiting",
                "label": "waiting",
                "value": receivedOrders.orders["WAITING"],
                "color": "hsl(228, 100%, 85%)" //blue
            },
            {
                "id": "confirmed",
                "label": "confirmed",
                "value": receivedOrders.orders["CONFIRMED"],
                "color": "hsl(209, 88%, 54%)" // blue light
            },
            {
                "id": "processing",
                "label": "processing",
                "value": receivedOrders.orders["PROCESSING"],
                "color": "hsl(44, 100%, 50%)" // orange
            },
            {
                "id": "shipped",
                "label": "shipped",
                "value": receivedOrders.orders["SHIPPED"],
                "color": "hsl(140, 88%, 43%)" // green
            },
            {
                "id": "refused",
                "label": "refused",
                "value": receivedOrders.orders["REFUSED"],
                "color": "hsl(358, 100%, 63%)" // red
            },
            {
                "id": "requireAction",
                "label": "action req",
                "value": receivedOrders.orders["REQUIRED_ACTION"],
                "color": "hsl(310, 88%, 77%)" // pink
            }
        ]


        return (
            <div className="chart-orders-number">
                <ResponsivePie
                    data={myData}
                    margin={{top: 40, right: 70, bottom: 80, left: 80}}
                    innerRadius={0.5}
                    padAngle={0.7}
                    cornerRadius={3}
                    colors={{datum: 'data.color'}}
                    borderWidth={1}
                    borderColor={{from: 'color', modifiers: [['darker', 0.2]]}}
                    radialLabelsSkipAngle={10}
                    radialLabelsTextColor="#333333"
                    radialLabelsLinkColor={{from: 'color'}}
                    sliceLabelsSkipAngle={10}
                    sliceLabelsTextColor="#333333"
                    legends={[
                        {
                            anchor: 'bottom',
                            direction: 'row',
                            justify: false,
                            translateX: -1,
                            translateY: 60,
                            itemsSpacing: 0,
                            itemWidth: 73,
                            itemHeight: 18,
                            itemTextColor: '#999',
                            itemDirection: 'left-to-right',
                            itemOpacity: 1,
                            symbolSize: 10,
                            symbolShape: 'circle',
                            effects: [
                                {
                                    on: 'hover',
                                    style: {
                                        itemTextColor: '#000'
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

const mapStateToProps = (state) => {
    return {
        ordersNumber: state.orders.ordersNumber
    }
};

export default connect(mapStateToProps)(OrdersNumberDashboard);