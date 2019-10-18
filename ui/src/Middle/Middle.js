import React, { Component } from 'react';
import Gallery from '../Gallery/Gallery';
import './Middle.css';

class Middle extends Component {
    state = {
        images: [
            { id: 1, name: 'Unknown1', url: 'Unknown1.png' },
            { id: 2, name: 'Unknown2', url: 'Unknown2.png' },
            { id: 3, name: 'Unknown3', url: 'Unknown3.png' }
        ]
    }
    render() {

        let gallery = (
            <div className="Container-Middle">
                {this.state.images.map((image, index) => {
                    return <Gallery url={image.url} key={index}/>
                })}
            </div>
        );

        return (
            <div>
                {gallery}
            </div>
        );
    }

}

export default Middle;