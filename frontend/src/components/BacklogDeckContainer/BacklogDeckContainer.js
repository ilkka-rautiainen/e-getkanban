import React from 'react';
import IconButton from 'material-ui/IconButton';
import ExpandMore from 'material-ui/svg-icons/navigation/expand-more';
import ExpandLess from 'material-ui/svg-icons/navigation/expand-less';
import BacklogDeck from "../BacklogDeck/BacklogDeck";
import "./BacklogDeckContainer.scss";

class BacklogDeckContainer extends React.Component {

  constructor() {
    super();
    this.state = {isVisible: false};

    this.handleClick = this.handleClick.bind(this);
  }

  handleClick() {
    this.setState(prevState => ({
      isVisible: !prevState.isVisible
    }));
  }

  get containerClass() {
     return this.state.isVisible ? "backlog-container" : "backlog-container closed";
  }

  render() {
    const styles = {
      width: 60,
      height: 60,
      padding: 0,
      color: '#fff',
    };
    return <div className={this.containerClass}>
      <h2>Backlog</h2>
      <BacklogDeck />
      <IconButton iconStyle={styles} style={styles} onClick={this.handleClick}>
        {this.state.isVisible ? <ExpandLess /> : <ExpandMore />}
      </IconButton>
    </div>
  }

};

export default BacklogDeckContainer;
