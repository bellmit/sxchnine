import React, {PureComponent} from "react";
import {connect} from 'react-redux';
import {Button, Dimmer, Dropdown, Form, Grid, Icon, Image, Label, Loader, Modal, Segment} from "semantic-ui-react";
import * as actions from "../../../../store/actions";
import Aux from '../../../../adhoc/Aux/Aux';
import './FormProduct.css';
import trash from './trash.png';

class FormProduct extends PureComponent {

    state = {
        id: this.props.productByIdData !== undefined ? this.props.productByIdData.id : this.generateId(),
        ref: this.props.productByIdData !== undefined ? this.props.productByIdData.ref : '',
        name: this.props.productByIdData !== undefined ? this.props.productByIdData.name : '',
        sex: this.props.productByIdData !== undefined ? this.props.productByIdData.sex : '',
        brand: this.props.productByIdData !== undefined ? this.props.productByIdData.brand : '',
        logo: this.props.productByIdData !== undefined ? this.props.productByIdData.logo : '',
        category: this.props.productByIdData !== undefined ? this.props.productByIdData.category : '',
        price: this.props.productByIdData !== undefined ? this.props.productByIdData.price : '',
        size: this.props.productByIdData !== undefined ? this.props.productByIdData.size : [],
        colors: this.props.productByIdData !== undefined ? this.props.productByIdData.colors : [],
        tags: this.props.productByIdData !== undefined ? this.props.productByIdData.tags : [],
        images: this.props.productByIdData !== undefined ? this.props.productByIdData.images : [],
        availability: this.props.productByIdData !== undefined ? this.props.productByIdData.availability : [],
        available: this.props.productByIdData !== undefined ? this.props.productByIdData.available : '',
        quantity: this.props.productByIdData !== undefined ? this.props.productByIdData.quantity : '',
        dimension: this.props.productByIdData !== undefined ? this.props.productByIdData.dimension : null,
        originalPrice: this.props.productByIdData !== undefined ? this.props.productByIdData.originalPrice : '',
        promotion: this.props.productByIdData !== undefined ? this.props.productByIdData.promotion : '',
        store: this.props.productByIdData !== undefined ? this.props.productByIdData.store : '',
        dateTime: this.props.productByIdData !== undefined ? this.props.productByIdData.dateTime : '',
        showDelete: true,
        showAddImage: false,
        newImage: '',
        showAddColor: false,
        newColor: '',
        newQte: 0,
        showAddAvailability: false,
        tagsOption: [],
        tag: ''
    }

    refreshState = () => {
        this.setState({
            ref: this.props.productByIdData !== undefined ? this.props.productByIdData.ref : '',
            name: this.props.productByIdData !== undefined ? this.props.productByIdData.name : '',
            sex: this.props.productByIdData !== undefined ? this.props.productByIdData.sex : '',
            brand: this.props.productByIdData !== undefined ? this.props.productByIdData.brand : '',
            logo: this.props.productByIdData !== undefined ? this.props.productByIdData.logo : '',
            category: this.props.productByIdData !== undefined ? this.props.productByIdData.category : '',
            price: this.props.productByIdData !== undefined ? this.props.productByIdData.price : '',
            size: this.props.productByIdData !== undefined ? this.props.productByIdData.size : [],
            colors: this.props.productByIdData !== undefined ? this.props.productByIdData.colors : [],
            tags: this.initializeTag(),
            images: this.props.productByIdData !== undefined ? this.props.productByIdData.images : [],
            availability: this.props.productByIdData !== undefined ? this.props.productByIdData.availability : [],
            available: this.props.productByIdData !== undefined ? this.props.productByIdData.available : '',
            quantity: this.props.productByIdData !== undefined ? this.props.productByIdData.quantity : '',
            dimension: this.props.productByIdData !== undefined ? this.props.productByIdData.dimension : null,
            originalPrice: this.props.productByIdData !== undefined ? this.props.productByIdData.originalPrice : '',
            promotion: this.props.productByIdData !== undefined ? this.props.productByIdData.promotion : '',
            store: this.props.productByIdData !== undefined ? this.props.productByIdData.store : '',
            dateTime: this.props.productByIdData !== undefined ? this.props.productByIdData.dateTime : '',
        });
    }

    initializeTag(){
        if (this.props.productByIdData !== undefined && this.props.productByIdData.tags !== null){
            return this.props.productByIdData.tags
        } else {
            return [];
        }
    }

    handleChange = (e, {name, value}) => this.setState({[name]: value});

    handleChangeSex = (e, {value}) => this.setState({value, sex: value});

    handleChangeCategory = (e, {value}) => this.setState({value, category: value});

    handleChangeStore = (e, {value}) => this.setState({value, store: value});

    handleChangeAvailable = (e, {value}) => this.setState({value, available: value});

    handleChangeSize = (e, {value}) => this.setState({size: value});

    handleChangeColor = (e, {value}) => this.setState({colors: value});

    handleTagAddition = (e, { value }) => {
        this.setState((prevState) => ({
            tags: [value, ...prevState.tags]
        }))
    };
    
    handleTagChange = (e, { value }) => this.setState({ tag: value });

    handleChangeNewQte = (e, {value}) => this.setState({newQte: value});

    closeModal = () => this.props.closeProductByIdPopup(this.props.history);

    showDeleteButton = () => this.setState({showDelete: !this.state.showDelete});

    showAddImage = () => this.setState({showAddImage: !this.state.showAddImage});

    showAddColor = () => this.setState({showAddColor: !this.state.showAddColor});

    showAddAvailability = () => {
        if (this.state.colors.length > 0 && this.state.size.length > 0){
            this.setState({showAddAvailability: !this.state.showAddAvailability});
            this.addNewAvailability();
        }
    }


    addNewImage = () => {
        let images = [...this.state.images];
        images.push(this.state.newImage);
        this.setState({
            images: images,
            showAddImage: false
        });
    };

    addNewColor = () => {
        let colors = [...this.state.colors];
        colors.push(this.state.newColor);
        this.setState({
            colors: colors,
            showAddColor: false
        });
        this.addAvailabilityByNewColor();
    }

    removeImage = (id) => {
        let images = [...this.state.images];
        images.splice(id, 1);
        this.setState({images: images});
    }

    checkAvailableIcon() {
        if (this.state.available) {
            return 'green'
        } else {
            return 'red'
        }
    }

    incrementSize(key, size) {
        let availabilityToSave = {...this.state.availability};
        let sizeQt = availabilityToSave[key];
        for (let sq of sizeQt) {
            if (sq.size === size) {
                sq.qte++;
            }
        }
        availabilityToSave[key] = sizeQt;
        this.setState({availability: availabilityToSave});
    }

    decrementSize(key, size) {
        let availabilityToSave = {...this.state.availability};
        let sizeQt = availabilityToSave[key];
        for (let sq of sizeQt) {
            if (sq.size === size) {
                sq.qte--;
            }
        }
        availabilityToSave[key] = sizeQt;
        this.setState({availability: availabilityToSave});
    }

    // For New Product
    addNewAvailability() {
        let currentAvailability = {...this.state.availability};
        this.state.colors.forEach((color, i) => currentAvailability[color] = Object.values(this.state.size).map((sz) => ({
            'size': sz,
            'qte': 0
        })));


        this.setState({availability: currentAvailability});
    }

    // for Edit Product
    addAvailabilityByNewColor() {
        if (this.props.editMode) {
            let currentAvailability = {...this.state.availability};
            let oldSize = [];
            for (var sz of this.state.size) {
                oldSize.push({'size': sz, 'qte': 0});
            }
            currentAvailability[this.state.newColor] = oldSize
            this.setState({availability: currentAvailability});
        }
    }

    saveProduct = () => {
        this.props.saveProduct(this.createProduct(), this.props.history);
    }

    createProduct() {
        return {
            id: this.state.id,
            ref: this.state.ref,
            name: this.state.name,
            sex: this.state.sex,
            brand: this.state.brand,
            logo: this.state.logo,
            category: this.state.category,
            price: this.state.price,
            size: this.state.size,
            colors: this.state.colors,
            tags: this.state.tags,
            images: this.state.images,
            availability: this.state.availability,
            available: this.state.available,
            quantity: this.state.quantity,
            dimension: this.state.dimension,
            originalPrice: this.state.originalPrice,
            promotion: this.state.promotion,
            store: this.state.store,
            dateTime: this.state.dateTime
        }
    }

    generateId(){
        return Date.now() + Math.random().toFixed(0);
    }


    render() {

        let optionsStore = [
            {key: 1, text: 'CA', value: 'CA'},
            {key: 2, text: 'FR', value: 'FR'}
        ];

        let optionsSize = [
            {key: 0, text: 'XSmall', value: 'XS'},
            {key: 1, text: 'Small', value: 'S'},
            {key: 2, text: 'Medium', value: 'M'},
            {key: 3, text: 'Large', value: 'L'},
            {key: 4, text: 'XLarge', value: 'XL'},
        ];

        let colors = [];
        if (this.state.colors) {
            this.state.colors.forEach(c => {
                colors.push({
                    key: c, text: c, value: c
                })
            });
        }

        let tagsOption = [];
        if (this.state.tags) {
            this.state.tags.forEach(c => {
                tagsOption.push({
                    key: c, text: c, value: c
                })
            });
        }

        let sexOptions = [
            {key: 0, text: '----', value: ''},
            {key: 1, text: 'Men', value: 'M'},
            {key: 2, text: 'Women', value: 'W'},
        ]

        let availableOptions = [
            {key: 1, text: 'Available', value: true},
            {key: 2, text: 'Sold out', value: false},
        ]

        let categories = [
            {key: 1, text: 'TShirt', value: 'Tshirt'},
            {key: 2, text: 'Sweat', value: 'Sweat'},
            {key: 3, text: 'Jacket', value: 'Jacket'},
            {key: 4, text: 'Hat', value: 'Hat'},
            {key: 5, text: 'Dresses', value: 'Dresses'},
            {key: 6, text: 'Accessories', value: 'Accessories'},
            {key: 7, text: 'Shorts', value: 'Shorts'},
            {key: 8, text: 'Pants', value: 'Pants'}
        ]

        let formAddImage = undefined;

        if (this.state.showAddImage) {
            formAddImage = <Form>
                <Form.Group inline>
                    <Form.Input width='10' fluid
                                placeholder='New image URL'
                                name='newImage'
                                icon='linkify'
                                value={this.state.newImage}
                                onChange={this.handleChange}
                    />
                    <Icon name="add"
                          color='green'
                          size='big'
                          className="product-delete-button"
                          onClick={this.addNewImage}/>
                </Form.Group>
            </Form>

        }


        let formAddColor = undefined;

        if (this.state.showAddColor) {
            formAddColor =
                <Form.Group inline>
                    <Form.Input width='10' fluid
                                placeholder='New Color'
                                name='newColor'
                                value={this.state.newColor}
                                onChange={this.handleChange}
                    />
                    <Icon name="add"
                          color='green'
                          size='big'
                          className="product-delete-button"
                          onClick={this.addNewColor}/>
                </Form.Group>
        }

        let availabilityBlock =
            <Form.Group grouped>
                <Grid>
                    {Object.keys(this.state.availability).map((key, idx) => (
                        <Grid.Row key={idx}>
                            <Grid.Column width={1}>
                                <Label size='small'
                                       basic
                                       color='green'>Color:</Label>
                            </Grid.Column>
                            <Grid.Column width={1}>
                                <span style={{fontWeight: 'bold'}}>{key}</span>
                            </Grid.Column>
                            <Grid.Column width={1}>
                                <Label size='small'
                                       basic
                                       color='green'>Size:</Label>
                            </Grid.Column>
                            {this.state.availability[key].map((av, indV) => (
                                <Aux key={indV}>
                                    <Grid.Column width={1}>
                                        <span style={{fontWeight: 'bold'}}>{av.size}</span>
                                    </Grid.Column>
                                    <Grid.Column width={1}>
                                        <span>{av.qte}</span>
                                    </Grid.Column>
                                    <Grid.Column width={1}>
                                        <Button circular icon='add' size='small' color='green'
                                                onClick={() => this.incrementSize(key, av.size)}/>
                                    </Grid.Column>
                                    <Grid.Column width={1}>
                                        <Button circular icon='delete' size='small' color='red'
                                                onClick={() => this.decrementSize(key, av.size)}/>
                                    </Grid.Column>
                                </Aux>
                            ))}
                        </Grid.Row>

                    ))}
                </Grid>
            </Form.Group>

        let buttonAddAvailability = undefined;
        if (this.state.availability.length === 0) {
            buttonAddAvailability = <Button
                style={{marginTop: '2%'}}
                icon='add'
                content="Add Availability"
                labelPosition='right'
                color='green'
                size='small'
                onClick={this.showAddAvailability}/>
        }

        let resumeAvailabilityForAddNewProduct = undefined;
        if (!this.props.editMode && Object.keys(this.state.availability).length > 0) {
            resumeAvailabilityForAddNewProduct =
                <Segment.Group>
                    <Segment>
                        <Icon name='boxes'/>
                        <span style={{fontWeight: 'bold'}}>Availability Summary</span>
                    </Segment>
                    <Segment.Group>
                        <Segment>
                            <Grid>
                                {Object.keys(this.state.availability).map((keyV, idxV) => (
                                    <Grid.Row key={idxV}>
                                        <Grid.Column width={2}>
                                            <Label color='red' basic content='Color:'/>
                                        </Grid.Column>
                                        <Grid.Column width={2}>
                                            <span style={{fontWeight: 'bold'}}>{keyV}</span>
                                        </Grid.Column>
                                        <Grid.Column width={2}>
                                            <Label color='red' basic content='Size:'/>
                                        </Grid.Column>
                                        {this.state.availability[keyV].map((avl, indV) => (
                                            <Grid.Column key={indV} width={4}>
                                                <Label color='green' circular inverted="true" content={avl.size}/>
                                                <Label color='green' circular inverted="true" content={avl.qte}/>
                                            </Grid.Column>
                                        ))}
                                    </Grid.Row>
                                ))}
                            </Grid>
                        </Segment>
                    </Segment.Group>
                </Segment.Group>
        }


        return (
            <Modal open={this.props.productByIdPopup}
                   onClose={this.closeModal}
                   size='large' onOpen={this.refreshState} onMount={this.refreshState}>
                <Dimmer active={this.props.saveProductLoading} page>
                    <Loader content='Loading'/>
                </Dimmer>
                <Modal.Content>
                    <Segment.Group>
                        <Segment>
                            <Icon name='archive'/>
                            <span style={{fontWeight: 'bold'}}>Product Info</span>
                        </Segment>
                        <Segment.Group>
                            <Segment>
                                <Form>
                                    <Form.Group widths='equal'>
                                        <Form.Input fluid
                                                    label='Product ID:'
                                                    name='id'
                                                    value={this.state.id}
                                                    disabled={this.props.editMode}
                                                    inverted
                                                    onChange={this.handleChange}/>
                                        <Form.Input fluid
                                                    label='Date Time:'
                                                    name='dateTime'
                                                    value={this.state.dateTime}
                                                    disabled/>
                                        <Image avatar size='small' src={this.state.logo}/>
                                    </Form.Group>
                                    <Form.Group widths='equal'>
                                        <Form.Input fluid
                                                    label='Ref:'
                                                    name='ref'
                                                    value={this.state.ref}
                                                    onChange={this.handleChange}/>
                                        <Form.Input fluid
                                                    label='Name:'
                                                    name='name'
                                                    value={this.state.name}
                                                    onChange={this.handleChange}/>
                                        <Form.Dropdown options={availableOptions}
                                                       style={{
                                                           fontWeight: 'bold',
                                                           color: this.checkAvailableIcon()
                                                       }}
                                                       label="Status:"
                                                       name='available'
                                                       value={this.state.available}
                                                       onChange={this.handleChangeAvailable}/>
                                    </Form.Group>
                                </Form>
                            </Segment>
                        </Segment.Group>

                        <Segment>
                            <Icon name='barcode'/>
                            <span style={{fontWeight: 'bold'}}>Product Details</span>
                        </Segment>
                        <Segment.Group>
                            <Segment>
                                <Form>
                                    <Form.Group widths='equal' inline>
                                        <Form.Dropdown onChange={this.handleChangeSex}
                                                       placeholder='Sex:'
                                                       label='Sex:'
                                                       options={sexOptions}
                                                       selection
                                                       value={this.state.sex}/>
                                        <Form.Dropdown onChange={this.handleChangeCategory}
                                                       placeholder='Category:'
                                                       label='Category:'
                                                       options={categories}
                                                       selection
                                                       value={this.state.category}/>
                                        <Form.Dropdown onChange={this.handleChangeStore}
                                                       placeholder='Store:'
                                                       label='Store:'
                                                       options={optionsStore}
                                                       selection
                                                       value={this.state.store}/>
                                    </Form.Group>
                                    <Form.Group inline widths='equal'>
                                        <Form.Input
                                            icon='dollar'
                                            label='Price:'
                                            name='price'
                                            value={this.state.price}
                                            onChange={this.handleChange}/>
                                        <Form.Input
                                            icon='dollar'
                                            label='Original Price:'
                                            name='originalPrice'
                                            value={this.state.originalPrice}
                                            onChange={this.handleChange}/>
                                        <Form.Input
                                            icon='tag'
                                            label='Promotion:'
                                            name='promotion'
                                            value={this.state.promotion}
                                            onChange={this.handleChange}/>

                                    </Form.Group>

                                    <Form.Group widths='equal' inline>
                                        <Form.Input fluid
                                                    label='Brand:'
                                                    name='brand'
                                                    value={this.state.brand}
                                                    onChange={this.handleChange}/>
                                        <Form.Input fluid
                                                    label='URL logo:'
                                                    name='logo'
                                                    icon='linkify'
                                                    value={this.state.logo}
                                                    onChange={this.handleChange}/>
                                    </Form.Group>
                                    <Form.Group>
                                        <Dropdown
                                            options={tagsOption}
                                            placeholder='Choose Tags:'
                                            search
                                            selection
                                            fluid
                                            multiple
                                            allowAdditions
                                            value={this.state.tag}
                                            onAddItem={this.handleTagAddition}
                                            onChange={this.handleTagChange}
                                        />
                                    </Form.Group>
                                    <Icon name='add'
                                          size='large'
                                          color='green'
                                          className="product-delete-button"
                                          onClick={this.showAddImage}/>
                                    <Icon name='delete'
                                          color='red'
                                          size='large'
                                          className="product-delete-button"
                                          onClick={this.showDeleteButton}/>
                                    {formAddImage}
                                    {this.state.images.map((image, idx) => (
                                        <Form.Group key={idx} inline>
                                            <Image size='small'
                                                   src={image}/>
                                            <Image size='mini'
                                                   src={trash}
                                                   className="product-delete-button"
                                                   disabled={this.state.showDelete}
                                                   onClick={() => this.removeImage(idx)}/>
                                        </Form.Group>
                                    ))}

                                    <Segment>
                                        <Icon name='shopping bag'/>
                                        <span style={{fontWeight: 'bold'}}>Availability</span>

                                        <Form.Group>
                                            <Dropdown
                                                placeholder='Size'
                                                fluid
                                                multiple
                                                selection
                                                options={optionsSize}
                                                value={this.state.size}
                                                onChange={this.handleChangeSize}/>
                                            <Dropdown
                                                placeholder='Colors'
                                                fluid
                                                multiple
                                                selection
                                                options={colors}
                                                value={this.state.colors}
                                                onChange={this.handleChangeColor}/>
                                            <Icon className="product-delete-button"
                                                  name='add'
                                                  color='green'
                                                  size='big'
                                                  onClick={this.showAddColor}/>
                                        </Form.Group>
                                        {formAddColor}
                                        {availabilityBlock}
                                        {resumeAvailabilityForAddNewProduct}
                                        {buttonAddAvailability}
                                    </Segment>
                                </Form>
                            </Segment>
                        </Segment.Group>
                    </Segment.Group>

                    <Modal.Actions>
                        <Button className="product-save-button"
                                color='black'
                                floated='right'
                                onClick={this.saveProduct}>
                            <span className="product-save-button-text">SAVE</span>
                            <Icon name='right chevron' color='yellow'/>
                        </Button>
                    </Modal.Actions>
                </Modal.Content>
            </Modal>
        );
    }
}

const mapStateToProps = state => {
    return {
        productByIdPopup: state.products.productByIdPopup,
        saveProductError: state.products.saveProductError,
        saveProductLoading: state.products.saveProductLoading
    }
}

const dispatchToProps = dispatch => {
    return {
        closeProductByIdPopup: (history) => dispatch(actions.closeProductModalAndRedirectBack(history)),
        saveProduct: (product, history) => dispatch(actions.saveProduct(product, history))
    }
}

export default connect(mapStateToProps, dispatchToProps)(FormProduct);