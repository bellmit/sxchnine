import React, {PureComponent} from 'react';
import {Icon, Menu} from 'semantic-ui-react';
import './Menu.css';

class AdminMenu extends PureComponent {

    state = {}

    setActiveItem(){
        if (this.props.history.location.pathname === '/manageOrders'){
            this.setState({activeItem: 'manageOrder'});
        } else if (this.props.history.location.pathname === '/historyOrders'){
            this.setState({activeItem: 'historyOrder'});
        } else if (this.props.history.location.pathname === '/home'){
            this.setState({activeItem: 'home'});
        } else if (this.props.history.location.pathname === '/manageProducts'){
            this.setState({activeItem: 'manageProducts'});
        } else if (this.props.history.location.pathname === '/bulkProducts'){
            this.setState({activeItem: 'bulkProducts'});

        }
    }

    componentDidMount() {
       this.setActiveItem();
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        this.setActiveItem();
    }

    handleItemClick = (e, {name}) => {
        this.setState({activeItem: name});

        if (name === 'home') {
            this.props.history.push('/home');
        } else if (name === 'manageOrder') {
            this.props.history.push('/manageOrders');
        } else if (name === 'historyOrder'){
            this.props.history.push('/historyOrders');
        } else if (name === 'manageProducts'){
            this.props.history.push('/manageProducts');
        } else if (name === 'bulkProducts'){
            this.props.history.push('/bulkProducts');
        }
    }

    render() {
        const {activeItem} = this.state

        return (
            <div className="Menu-Form-Div">
                <Menu vertical inverted color='yellow' size='small'>
                    <Menu.Item>
                        <Menu.Header>
                            <Icon name='home' color='black'/>
                            <span className="Menu-Text">Home</span>
                        </Menu.Header>
                        <Menu.Menu>
                            <Menu.Item
                                name='home'
                                active={activeItem === 'home'}
                                onClick={this.handleItemClick}>
                                <span className="Menu-Text">Home</span>
                            </Menu.Item>
                        </Menu.Menu>
                    </Menu.Item>
                    <Menu.Item>
                        <Menu.Header>
                            <Icon name='cart arrow down' color='black'/>
                            <span className="Menu-Text">Orders</span>
                        </Menu.Header>
                        <Menu.Menu>
                            <Menu.Item
                                name='manageOrder'
                                active={activeItem === 'manageOrder'}
                                onClick={this.handleItemClick}>
                                <span className="Menu-Text">Manage</span>
                            </Menu.Item>
                            <Menu.Item
                                name='historyOrder'
                                active={activeItem === 'historyOrder'}
                                onClick={this.handleItemClick}>
                                <span className="Menu-Text">History</span>
                            </Menu.Item>
                        </Menu.Menu>
                    </Menu.Item>

                    <Menu.Item>
                        <Menu.Header>
                            <Icon name='barcode' color='black'/>
                            <span className="Menu-Text">Products</span>
                        </Menu.Header>

                        <Menu.Menu>
                            <Menu.Item
                                name='manageProducts'
                                active={activeItem === 'manageProducts'}
                                onClick={this.handleItemClick}>
                                <span className="Menu-Text">Manage</span>
                            </Menu.Item>
                            <Menu.Item
                                name='bulkProducts'
                                active={activeItem === 'bulkProducts'}
                                onClick={this.handleItemClick}>
                                <span className="Menu-Text">Bulk</span>
                            </Menu.Item>
                            <Menu.Item
                                name='category'
                                active={activeItem === 'category'}
                                onClick={this.handleItemClick}>
                                <span className="Menu-Text">Category</span>
                            </Menu.Item>
                        </Menu.Menu>
                    </Menu.Item>

                    <Menu.Item>
                        <Menu.Header>
                            <Icon name='users' color='black'/>
                            <span className="Menu-Text">Users</span>
                        </Menu.Header>

                        <Menu.Menu>
                            <Menu.Item
                                name='manageUsers'
                                active={activeItem === 'manageUsers'}
                                onClick={this.handleItemClick}>
                                <span className="Menu-Text">Manage</span>
                            </Menu.Item>
                        </Menu.Menu>
                    </Menu.Item>

                    <Menu.Item>
                        <Menu.Header>
                            <Icon name='mail' color='black'/>
                            <span className="Menu-Text">Email</span>
                        </Menu.Header>

                        <Menu.Menu>
                            <Menu.Item
                                name='email'
                                active={activeItem === 'email'}
                                onClick={this.handleItemClick}>
                                <span className="Menu-Text">E-mail Support</span>
                            </Menu.Item>

                            <Menu.Item
                                name='newsletter'
                                active={activeItem === 'newsletter'}
                                onClick={this.handleItemClick}>
                                <span className="Menu-Text">Newsletter</span>
                            </Menu.Item>
                        </Menu.Menu>
                    </Menu.Item>

                    <Menu.Item>
                        <Menu.Header>
                            <Icon name='payment' color='black'/>
                            <span className="Menu-Text">Payment</span>
                        </Menu.Header>

                        <Menu.Menu>
                            <Menu.Item
                                name='refund'
                                active={activeItem === 'refund'}
                                onClick={this.handleItemClick}>
                                <span className="Menu-Text">Refund</span>
                            </Menu.Item>
                        </Menu.Menu>
                    </Menu.Item>
                </Menu>
            </div>
        );
    }
}

export default AdminMenu;