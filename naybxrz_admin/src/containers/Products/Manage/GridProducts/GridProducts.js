import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Dimmer, Icon, Image, Label, List, Loader, Table} from "semantic-ui-react";
import Aux from '../../../../adhoc/Aux/Aux';
import './GridProducts.css';
import * as actions from "../../../../store/actions";
import EditProduct from "../EditProduct/EditProduct";

class GridProducts extends Component {

    componentDidUpdate(prevProps, prevState, snapshot) {
        console.log(this.props.searchProductsError)
    }

    checkAvailableIcon(available){
        if (available){
            return <Icon name='check' color='green' size='large' />
        } else {
            return <Icon name='close' color='red' size='large' />
        }
    }

    checkSexIcon(sex){
        if (sex === 'M'){
            return <Icon name='male' color='blue' size='large' />
        } else {
            return <Icon name='female' color='pink' size='large' />
        }
    }

    handleProduct = (productId) => {
        this.props.getProductById(productId);
    }

    render() {

        let errors = undefined;

        if (this.props.searchProductsError){
            errors = <Label color='red'>{this.props.searchProductsError.message}</Label>;
        }

        let headers = <Table.Header>
            <Table.Row>
                <Table.HeaderCell>ID</Table.HeaderCell>
                <Table.HeaderCell>Ref.</Table.HeaderCell>
                <Table.HeaderCell>Brand</Table.HeaderCell>
                <Table.HeaderCell>Name</Table.HeaderCell>
                <Table.HeaderCell>Price</Table.HeaderCell>
                <Table.HeaderCell>Promotion Price</Table.HeaderCell>
                <Table.HeaderCell>Category</Table.HeaderCell>
                <Table.HeaderCell>Sex</Table.HeaderCell>
                <Table.HeaderCell>Size</Table.HeaderCell>
                <Table.HeaderCell>Colors</Table.HeaderCell>
                <Table.HeaderCell>Dimension</Table.HeaderCell>
                <Table.HeaderCell>Qte.</Table.HeaderCell>
                <Table.HeaderCell>Store</Table.HeaderCell>
                <Table.HeaderCell>Available</Table.HeaderCell>
                <Table.HeaderCell>Date</Table.HeaderCell>
            </Table.Row>
        </Table.Header>

        let body = undefined;

        if (this.props.searchProductsData) {
            body = <Aux>
                <Table color='olive' size='small' collapsing compact>
                    {headers}
                    {this.props.searchProductsData.map((p, index) => (
                        <Table.Body key={index}>
                            <Table.Row>
                                <Table.Cell selectable>
                                        <span className="search-products-id-text"
                                              onClick={() => this.handleProduct(p.id)}>{p.id}</span>
                                </Table.Cell>
                                <Table.Cell>{p.ref}</Table.Cell>
                                <Table.Cell>
                                    <List>
                                        <List.Item>
                                            <List.Content>
                                                <List.Header>
                                                    <Image avatar src = {p.logo} size='mini' />
                                                </List.Header>
                                                <List.Description>
                                                    {p.brand}
                                                </List.Description>
                                            </List.Content>
                                        </List.Item>
                                    </List>
                                </Table.Cell>

                                <Table.Cell>
                                    <List>
                                        <List.Item>
                                            <List.Content>
                                                <List.Header>
                                                    <Image avatar src = {p.images[0]} size='mini' />
                                                </List.Header>
                                                <List.Description>
                                                    {p.name}
                                                </List.Description>
                                            </List.Content>
                                        </List.Item>
                                    </List>
                                </Table.Cell>
                                <Table.Cell>
                                    <Label tag color='red' size='mini'>{p.price}</Label>
                                </Table.Cell>
                                <Table.Cell>
                                    <Label tag color='olive' size='mini'>{p.originalPrice}</Label>
                                </Table.Cell>
                                <Table.Cell>{p.category}</Table.Cell>
                                <Table.Cell>
                                    {this.checkSexIcon(p.sex)}
                                </Table.Cell>
                                <Table.Cell>
                                {p.size.map((size, idxS) => (
                                    <List key={idxS}>
                                        <List.Item>
                                            <List.Content>
                                                <List.Description>
                                                    {size}
                                                </List.Description>
                                            </List.Content>
                                        </List.Item>
                                    </List>
                                ))}
                                </Table.Cell>
                                <Table.Cell>
                                {p.colors.map((color, idxC) => (
                                    <List key={idxC}>
                                        <List.Item>
                                            <List.Content>
                                                <List.Description>
                                                    {color}
                                                </List.Description>
                                            </List.Content>
                                        </List.Item>
                                    </List>
                                ))}
                                </Table.Cell>
                                <Table.Cell singleLine>h: x w: </Table.Cell>
                                <Table.Cell>{p.quantity}</Table.Cell>
                                <Table.Cell>{p.store}</Table.Cell>
                                <Table.Cell>{this.checkAvailableIcon(p.available)}</Table.Cell>
                                <Table.Cell singleLine>{p.dateTime}</Table.Cell>
                            </Table.Row>
                        </Table.Body>
                    ))}
                </Table>
            </Aux>
        }

        return (
            <div className="table-search-products">
                <Dimmer active={this.props.loading} page>
                    <Loader content='Loading'/>
                </Dimmer>
                {errors}
                {body}

                <EditProduct />
            </div>
        );
    }
}


const mapStateToProps = state => {
    return {
        loading: state.products.searchProductsLoading,
        searchProductsData: state.products.searchProductsData,
        searchProductsError: state.products.searchProductsError
    }
}

const dispatchToProps = dispatch => {
    return {
        getProductById: (productId) => dispatch(actions.getProductById(productId)),
    }
}

export default connect(mapStateToProps, dispatchToProps)(GridProducts);